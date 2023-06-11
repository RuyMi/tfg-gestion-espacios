package es.dam.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de la clase User.
 * Contiene los DTOs de User, UserCreate, UserUpdate, UserLogin, UserToken, UserData, UserResponse y UserPhoto
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Serializable
data class UserRegisterDTO(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String? = null,
    val isActive: Boolean,
    val userRole: Set<String>
)

@Serializable
data class UserUpdateDTO(
    val name: String,
    val username: String,
    val avatar: String? = null,
    val isActive: Boolean,
    val userRole: Set<String>,
    val credits: Int,
)

@Serializable
data class UserLoginDTO(
    val username: String,
    val password: String
)

@Serializable
data class UserTokenDTO(
    val user: UserResponseDTO,
    val token: String
)

@Serializable
data class UserResponseDTO(
    val uuid: String,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String? = null,
    val userRole: Set<String>,
    val credits: Int,
    val isActive: Boolean
)

@Serializable
data class UserDataDTO(
    val data: List<UserResponseDTO>?
)

@Serializable
data class UserPhotoDTO(
    val data: Map<String, String>
)

