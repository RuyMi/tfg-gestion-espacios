package es.dam.exceptions

sealed class BookingExceptions(message: String) : RuntimeException(message)

class BookingNotFoundException(message: String) : SpaceExceptions(message)
class BookingBadRequestException(message: String) : SpaceExceptions(message)
class BookingConflictIntegrityException(message: String) : SpaceExceptions(message)