package es.dam.exceptions

/**
 * Clase que implementa las excepciones personalizadas de los espacios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class BookingExceptions(message: String) : RuntimeException(message)

class BookingNotFoundException(message: String) : SpaceExceptions(message)
class BookingBadRequestException(message: String) : SpaceExceptions(message)
class BookingConflictIntegrityException(message: String) : SpaceExceptions(message)
class BookingMediaNotSupportedException(message: String) : SpaceExceptions(message)
class BookingInternalErrorException(message: String) : SpaceExceptions(message)