package es.dam.microserviciousuarios

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
class MicroservicioUsuariosApplication

fun main(args: Array<String>) {
    runApplication<MicroservicioUsuariosApplication>(*args)
}
