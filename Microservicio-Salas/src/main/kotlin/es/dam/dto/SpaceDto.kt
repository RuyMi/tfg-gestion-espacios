package es.dam.dto

import es.dam.models.Space
import kotlinx.serialization.Serializable

/**
 * DTOs de la clase Space.
 * Contiene los DTOs de Space, SpaceCreate, SpaceUpdate, SpaceData, SpaceResponse y SpacePhoto
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

@Serializable
data class SpaceDTO(
    val id: String,
    val uuid: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val authorizedRoles: Set<String>,
    val bookingWindow: Int
)

@Serializable
data class SpaceCreateDTO(
    val name: String,
    val description: String? = "",
    val image: String? = null,
    val price: Int,
    val isReservable: Boolean = false,
    val requiresAuthorization: Boolean,
    val authorizedRoles: Set<String>,
    val bookingWindow: Int
)

@Serializable
data class SpaceUpdateDTO(
    val name: String,
    val description: String? = "",
    val image: String? = null,
    val price: Int,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val authorizedRoles: Set<String> = setOf(Space.UserRole.USER.toString()),
    val bookingWindow: Int
)

@Serializable
data class SpaceDataDTO(
    val data: List<SpaceDTO>
)

@Serializable
data class SpaceResponseDTO(
    val id: String,
    val uuid: String,
    val name: String,
    val description: String,
    val image: String?,
    val price: Int,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val authorizedRoles: Set<String>,
    val bookingWindow: Int
)

@Serializable
data class SpacePhotoDTO(
    val data: Map<String, String>
)