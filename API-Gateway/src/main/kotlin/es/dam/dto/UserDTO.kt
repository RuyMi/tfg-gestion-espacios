package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val uuid: String,
    val name: String,
    val username: String,
    val avatar: String,
    val userRole: String,
    val email: String
)

@Serializable
data class UserUpdateDTO(
    val name: String,
    val username: String,
    val email: String,
    val avatar: String,
    val userRole: String,
    val image: String,
    val password: String,
)

@Serializable
data class UserLoginDTO(
    val username: String,
    val password: String
)

@Serializable
data class UserRegisterDTO(
    val name: String,
    val username: String,
    val email: String
)

@Serializable
data class UserDataDTO(
    val data: List<UserResponseDTO>
)

@Serializable
data class UserTokenDTO(
    val user: UserResponseDTO,
    val token: String
)

