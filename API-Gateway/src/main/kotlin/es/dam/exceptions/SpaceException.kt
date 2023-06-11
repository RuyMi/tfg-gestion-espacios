package es.dam.exceptions

/**
 * Clase que implementa las excepciones personalizadas de los espacios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class SpaceExceptions(message: String) : RuntimeException(message)

class SpaceNotFoundException(message: String) : SpaceExceptions(message)
class SpaceBadRequestException(message: String) : SpaceExceptions(message)
class SpaceConflictIntegrityException(message: String) : SpaceExceptions(message)
class SpaceInternalErrorException(message: String) : SpaceExceptions(message)