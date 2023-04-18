package es.dam.microserviciousuarios.dto

import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserDTO(
    val id: Long,
    val uuid: String,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val userRole: Set<User.UserRole>,
    val metadata: Metadata
) {
    @Serializable
    data class Metadata(
        @Serializable(with = LocalDateTimeSerializer::class)
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        @Serializable(with = LocalDateTimeSerializer::class)
        val updatedAt: LocalDateTime? = LocalDateTime.now()
    )
}

@Serializable
data class UserRegisterDTO(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val userRole: Set<User.UserRole>
)

@Serializable
data class UserUpdateDTO(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val userRole: Set<User.UserRole>
)

@Serializable
data class UserLoginDTO(
    val username: String,
    val password: String
)

@Serializable
data class UserResponseDTO(
    val id: Long,
    val uuid: String,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val avatar: String?,
    val userRole: Set<User.UserRole>
)

@Serializable
data class UserDataDTO(
    val data: List<UserResponseDTO>
)
