package es.dam.validators

import es.dam.dto.SpaceCreateDTO
import io.ktor.server.plugins.requestvalidation.*

/**
 * Función que valida los datos de un espacio.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

fun RequestValidationConfig.spaceValidation(){
    validate<SpaceCreateDTO> {
        if(it.name.isEmpty()){
            ValidationResult.Invalid("El nombre no puede estar vacío")
        }else if(it.price <= 0){
            ValidationResult.Invalid("El precio debe ser mayor que cero")
        }else if(it.authorizedRoles.isEmpty()){
            ValidationResult.Invalid("Los roles autorizados no pueden estar vacíos")
        }else if(it.bookingWindow <= 0){
            ValidationResult.Invalid("La ventana de reserva debe ser mayor que cero")
        }else {
            ValidationResult.Valid
        }
    }
}