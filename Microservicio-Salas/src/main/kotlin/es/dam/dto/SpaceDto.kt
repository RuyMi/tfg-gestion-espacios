package es.dam.dto

import es.dam.models.Space
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SpaceDto(
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
data class SpaceDtoCreate(
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<String>?,
    val bookingWindow: Duration?
)

@Serializable
data class SpaceDtoUpdate(
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
data class SpaceAllDto(
    val data: List<SpaceDto>
)