package es.dam.microserviciousuarios.security

import es.dam.microserviciousuarios.security.jwt.JWTAuthenticationFilter
import es.dam.microserviciousuarios.security.jwt.JWTAuthorizationFilter
import es.dam.microserviciousuarios.service.UserService
import es.dam.microserviciousuarios.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig
@Autowired constructor(
    private val userService: UserService,
    private val jwtUtils: JWTUtils
) {

    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder::class.java
        )

        authenticationManagerBuilder.userDetailsService(userService)
        return authenticationManagerBuilder.build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationManager = authManager(http)

        http
            .csrf()
            .disable()
            .exceptionHandling()
            .and()

            .authenticationManager(authenticationManager)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeHttpRequests()
            .requestMatchers("/error/**").permitAll()
            .requestMatchers("/users/login", "/users/register").permitAll()
            .requestMatchers("/**").permitAll()
            .requestMatchers("/users", "/users{id}").hasAnyRole("ADMINISTRATOR")
            .anyRequest().authenticated()

            .and()
            .addFilter(JWTAuthenticationFilter(jwtUtils, authenticationManager))
            .addFilter(JWTAuthorizationFilter(jwtUtils, userService, authenticationManager))

        return http.build()
    }
}