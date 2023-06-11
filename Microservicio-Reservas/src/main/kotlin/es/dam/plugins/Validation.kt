package es.dam.plugins

import es.dam.validators.bookingValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

/**
 * Configuración de la validación del microservicio.
 *
 * @autor Mireya Sánchez Pinzón
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 */
fun Application.configureValidation(){
    install(RequestValidation) {
        bookingValidation()
    }
}
