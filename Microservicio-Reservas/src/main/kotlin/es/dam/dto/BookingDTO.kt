package es.dam.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val id: String,
    val uuid:String,
    val userId: String,
    val spaceId: String,
    val startTime: String,
    val endTime: String,
    val phone: String?,
    val status: String
)

@Serializable
data class BookingDtoCreate(
    val userId: String,
    val spaceId: String,
    val startTime: String,
    val endTime: String,
    val phone: String? = null,
    val status: String = "PENDING"
)

@Serializable
data class BookingDtoUpdate(
    val userId: String,
    val spaceId: String,
    val startTime: String,
    val endTime: String,
    val phone: String?,
    val status: String
)

@Serializable
data class BookingAllDto(
    val data: List<BookingDto>
)