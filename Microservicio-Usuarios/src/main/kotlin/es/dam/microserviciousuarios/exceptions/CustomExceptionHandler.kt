package es.dam.microserviciousuarios.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

/**
 * Clase que gestiona las excepciones de la API REST de usuarios.
 * Devuelve un mensaje de error y el código de error HTTP correspondiente.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleUserBadRequestException(ex: ResponseStatusException, request: HttpServletRequest): ResponseEntity<Any> {
        return ResponseEntity(ex.reason, ex.statusCode)
    }
}

