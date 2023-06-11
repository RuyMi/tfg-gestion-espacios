package es.dam.microserviciousuarios

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

/**
 * Clase principal de la aplicación. Se encarga de ejecutar la aplicación.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@SpringBootApplication
@EnableWebSecurity
class MicroservicioUsuariosApplication

fun main(args: Array<String>) {
    runApplication<MicroservicioUsuariosApplication>(*args)
}
