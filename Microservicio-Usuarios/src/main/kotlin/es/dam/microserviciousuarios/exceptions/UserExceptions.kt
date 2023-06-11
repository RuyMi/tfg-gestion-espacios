package es.dam.microserviciousuarios.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Clase que gestiona las excepciones de los usuarios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class UserExceptions(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserBadRequestException(message: String) : RuntimeException(message)