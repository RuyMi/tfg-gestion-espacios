package es.dam.validators


import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpaceDTO
import io.ktor.server.plugins.requestvalidation.*


fun RequestValidationConfig.spaceValidation(){
    validate<SpaceCreateDTO> {
        if(it.name.isEmpty()){
            ValidationResult.Invalid("The name cannot be empty")
        }else if(it.authorizedRoles.isEmpty()){
            ValidationResult.Invalid("The authorized roles cannot be empty")
            //TODO Mirar si el Duration tiene ese regex
        }else {
            ValidationResult.Valid
        }
    }
}