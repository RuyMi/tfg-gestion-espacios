package es.dam.microserviciousuarios.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Clase que gestiona las excepciones de los tokens del microservicio de usuarios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class TokenException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class TokenInvalidException(message: String) : TokenException(message)