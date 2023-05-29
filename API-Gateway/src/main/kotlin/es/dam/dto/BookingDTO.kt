package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingResponseDTO(
    val uuid: String,
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String? = null,
    val startTime: String,
    val endTime: String,
    val observations: String? = null,
    val status: String
)

@Serializable
data class BookingCreateDTO(
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String? = null,
    val startTime: String,
    val endTime: String,
    val observations: String? = null,
    val status: String? = "PENDING"
)

@Serializable
data class BookingDataDTO(
    val data: List<BookingResponseDTO>
)

@Serializable
data class BookingUpdateDTO(
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String? = null,
    val startTime: String,
    val endTime: String,
    val observations: String? = null,
    val status: String
)