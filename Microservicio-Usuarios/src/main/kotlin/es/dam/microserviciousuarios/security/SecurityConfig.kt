package es.dam.microserviciousuarios.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.web.authentication.AuthenticationConverter


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Value("\${azure.activedirectory.client-id}")
    private val audience: String = ""

    @Value("\${azure.activedirectory.tenant-id}")
    private val issuer: String = ""

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val jwtDecoder = JwtDecoders.fromIssuerLocation(issuer) as NimbusJwtDecoder
        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }

    fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .requestMatchers("/error/**").permitAll()
            .requestMatchers("/users/login", "/users/register").permitAll()
            .requestMatchers("/**").permitAll()
            .requestMatchers("/users", "/users{id}").hasAnyRole("ADMINISTRATOR")
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt()
    }
}
