package es.dam.microserviciousuarios.mappers

import es.dam.microserviciousuarios.dto.UserRegisterDTO
import es.dam.microserviciousuarios.dto.UserResponseDTO
import es.dam.microserviciousuarios.models.User

/**
 * Clase que mapea los DTOs de User. Utilizados para convertir de User a UserResponseDTO y viceversa.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun User.toDTO(): UserResponseDTO {
    return UserResponseDTO(
        id = id.toString(),
        uuid = uuid.toString(),
        avatar = avatar,
        userRole = userRole.split(",").map { it.trim() }.toSet(),
        email = email,
        username = username,
        password = password,
        name = name,
        credits = credits,
        isActive = isActive
    )
}

fun UserRegisterDTO.toModel(): User {
    return User(
        avatar = avatar,
        userRole = userRole.joinToString(", ") { it.uppercase().trim() },
        email = email,
        username = username,
        password = password,
        name = name
    )
}