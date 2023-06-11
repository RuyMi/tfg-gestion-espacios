package es.dam.exceptions

/**
 * Clase que implementa las excepciones personalizadas de los usuarios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class UserExceptions(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : UserExceptions(message)
class UserBadRequestException(message: String) : UserExceptions(message)
class UserUnauthorizedException(message: String) : UserExceptions(message)
class UserConflictIntegrityException(message: String) : UserExceptions(message)
class UserInternalErrorException(message: String) : UserExceptions(message)