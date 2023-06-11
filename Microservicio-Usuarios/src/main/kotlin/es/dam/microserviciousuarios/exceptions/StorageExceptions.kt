package es.dam.microserviciousuarios.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Clase que gestiona las excepciones del Storage.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class StorageException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class StorageNotFoundException(message: String) : StorageException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class StorageBadRequestException(message: String) : StorageException(message)