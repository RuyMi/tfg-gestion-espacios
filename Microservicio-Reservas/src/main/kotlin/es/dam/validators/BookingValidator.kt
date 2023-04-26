package es.dam.validators

import es.dam.dto.BookingDtoCreate
import io.ktor.server.plugins.requestvalidation.*


fun RequestValidationConfig.bookingValidation(){
    validate<BookingDtoCreate> {
        if(it.userId.isEmpty()){
            ValidationResult.Invalid("The user id cannot be empty")
        }else if (it.spaceId.isEmpty()){
            ValidationResult.Invalid("The space id cannot be empty")
        }//else if (it.startTime.isEmpty() || !it.startTime.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-5][0-9]:[0-5][0-9]$".toRegex())) {
           // ValidationResult.Invalid("The date cannot be empty or is invalid")
        //}else if (it.endTime < it.startTime){
          //  ValidationResult.Invalid("The end time cannot be less than the start time")
        //}else if( it.endTime.isEmpty()||it.endTime.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-5][0-9]:[0-5][0-9]$".toRegex())){
          //  ValidationResult.Invalid("The date cannot be empty or is invalid")
        else {
            ValidationResult.Valid
        }
    }
}