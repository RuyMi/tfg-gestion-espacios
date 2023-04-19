package es.dam.dto

import es.dam.models.Space
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SpaceDTO(
    val id: String,
    val uuid: String,
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<Space.UserRole>,
    val bookingWindow: Duration
)

@Serializable
data class SpaceDTOCreate(
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<String>?,
    val bookingWindow: Duration?
)

@Serializable
data class SpaceDTOUpdate(
    val id: String,
    val uuid: String,
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<String>?,
    val bookingWindow: Duration?
)

@Serializable
data class SpaceAllDTO(
    val data: List<SpaceDTO>
)