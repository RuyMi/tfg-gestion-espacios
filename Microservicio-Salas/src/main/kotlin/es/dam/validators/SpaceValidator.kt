package es.dam.validators


import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpaceDTO
import io.ktor.server.plugins.requestvalidation.*


fun RequestValidationConfig.bookingValidation(){
    validate<SpaceCreateDTO> {
        if(it.name.isEmpty()){
            ValidationResult.Invalid("The name cannot be empty")
        }else if(it.authorizedRoles.isEmpty()){
            ValidationResult.Invalid("The authorized roles cannot be empty")
            //TODO Mirar si el Duration tiene ese regex
        }else if(it.bookingWindow.isEmpty() || !it.bookingWindow.matches("^[0-9]{2}:[0-9]{2}$".toRegex())){
            ValidationResult.Invalid("The booking window cannot be empty")
        } else {
            ValidationResult.Valid
        }
    }
}