package es.dam.exceptions

sealed class UserExceptions(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : UserExceptions(message)
class UserBadRequestException(message: String) : UserExceptions(message)
class UserUnauthorizedException(message: String) : UserExceptions(message)
class UserConflictIntegrityException(message: String) : UserExceptions(message)
class UserInternalErrorException(message: String) : UserExceptions(message)