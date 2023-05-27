package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val uuid:String,
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String?,
    val startTime: String,
    val endTime: String,
    val observations: String?,
    val status: String
)

@Serializable
data class BookingDtoCreate(
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String?,
    val startTime: String,
    val endTime: String,
    val observations: String?,
    val status: String = "PENDING"
)

@Serializable
data class BookingDtoUpdate(
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String?,
    val startTime: String,
    val endTime: String,
    val observations: String?,
    val status: String
)

@Serializable
data class BookingAllDto(
    val data: List<BookingDto>
)