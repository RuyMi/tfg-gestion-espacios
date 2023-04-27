package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpaceResponseDTO(
    val uuid: String,
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<String>,
    val bookingWindow: String
)

@Serializable
data class SpaceDataDTO(
    val data: List<SpaceResponseDTO>
)

@Serializable
data class SpaceCreateDTO(
    val name: String,
    val isReservable: Boolean = false,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<String>,
    val bookingWindow: String
)


@Serializable
data class SpaceUpdateDTO(
    val name: String?,
    val isReservable: Boolean?,
    val requiresAuthorization: Boolean?,
    val maxBookings: Int?,
    val authorizedRoles: Set<String?>,
    val bookingWindow: String?
)