package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseDTO(
    val uuid: String,
    val userId: String,
    val spaceId: String,
    val startTime: String,
    val endTime: String,
    val phone: String?,
    val status: String
)

@Serializable
data class BookingCreateDTO(
    val userId: String,
    val spaceId: String,
    val startTime: String,
    val endTime: String,
    val phone: String?,
    val status: String = "PENDING"
)

@Serializable
data class BookingDataDTO(
    val data: List<BookingResponseDTO>
)

@Serializable
data class BookingUpdateDTO(
    val userId: String?,
    val spaceId: String?,
    val startTime: String?,
    val endTime: String?,
    val phone: String?,
    val status: String?
)