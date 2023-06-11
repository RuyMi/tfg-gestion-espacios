package es.dam.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de la clase de Bookings contiene los DTO de BookingDto, BookingDtoCreate, BookingDtoUpdate y BookingAllDto.
 * BookingDto: DTO de la clase Booking.
 * BookingDtoCreate: DTO de la clase Booking para crear una reserva.
 * BookingDtoUpdate: DTO de la clase Booking para actualizar una reserva.
 * BookingAllDto: DTO de la clase Booking para mostrar todas las reservas.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
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
    val image: String = "placeholder.jpeg",
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