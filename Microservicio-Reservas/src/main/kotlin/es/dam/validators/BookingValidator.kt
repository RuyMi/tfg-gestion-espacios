package es.dam.validators

import es.dam.dto.BookingDtoCreate
import io.ktor.server.plugins.requestvalidation.*


/**
 * Configuración de la validación de reservas.
 *
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 * @autor Mireya Sánchez Pinzón
 */
fun RequestValidationConfig.bookingValidation(){
    validate<BookingDtoCreate> {
        if(it.userId.isEmpty()){
            ValidationResult.Invalid("The user id cannot be empty")
        }else if (it.spaceId.isEmpty()){
            ValidationResult.Invalid("The space id cannot be empty")
        } else {
            ValidationResult.Valid
        }
    }
}