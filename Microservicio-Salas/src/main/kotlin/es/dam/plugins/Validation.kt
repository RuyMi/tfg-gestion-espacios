package es.dam.plugins

import es.dam.validators.spaceValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

/**
 * Configuración de la validación del microservicio.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

fun Application.configureValidation(){
    install(RequestValidation) {
        spaceValidation()
    }
}
