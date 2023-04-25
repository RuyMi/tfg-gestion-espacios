package es.dam.plugins

import es.dam.validators.spaceValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*


fun Application.configureValidation(){
    install(RequestValidation) {
        spaceValidation()
    }
}
