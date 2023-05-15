package es.dam.exceptions

sealed class SpaceExceptions(message: String) : RuntimeException(message)

class SpaceNotFoundException(message: String) : SpaceExceptions(message)
class SpaceBadRequestException(message: String) : SpaceExceptions(message)
class SpaceConflictIntegrityException(message: String) : SpaceExceptions(message)
class SpaceInternalErrorException(message: String) : SpaceExceptions(message)