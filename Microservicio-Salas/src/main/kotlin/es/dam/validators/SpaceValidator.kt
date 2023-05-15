package es.dam.validators


import es.dam.dto.SpaceCreateDTO
import io.ktor.server.plugins.requestvalidation.*


fun RequestValidationConfig.spaceValidation(){
    validate<SpaceCreateDTO> {
        if(it.name.isEmpty()){
            ValidationResult.Invalid("The name cannot be empty")
        }else if(it.price <= 0){
            ValidationResult.Invalid("The price must be greater than zero")
        }else if(it.authorizedRoles.isEmpty()){
            ValidationResult.Invalid("The authorized roles cannot be empty")
            //TODO Algo para comprobar el regex del duration
        }else if(it.bookingWindow.isEmpty()){
            ValidationResult.Invalid("The booking window cannot be empty")
        }else {
            ValidationResult.Valid
        }
    }
}