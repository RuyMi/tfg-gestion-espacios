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
        }else if(it.bookingWindow <= 0){
            ValidationResult.Invalid("The booking window must be greater than zero")
        }else {
            ValidationResult.Valid
        }
    }
}