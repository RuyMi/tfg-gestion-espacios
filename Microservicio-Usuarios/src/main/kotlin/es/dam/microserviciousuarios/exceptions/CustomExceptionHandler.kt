package es.dam.microserviciousuarios.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleUserBadRequestException(ex: ResponseStatusException, request: HttpServletRequest): ResponseEntity<Any> {
        return ResponseEntity(ex.reason, ex.statusCode)
    }
}

