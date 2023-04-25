package es.dam.mappers

import es.dam.dto.BookingDtoCreate
import es.dam.dto.BookingDtoUpdate
import es.dam.models.Booking
import java.time.LocalDateTime


fun BookingDtoCreate.toModel() = Booking(
    spaceId = this.spaceId,
    userId = this.userId,
    startTime = LocalDateTime.of(
        this.startTime.substring(0, 4).toInt(),
        this.startTime.substring(5, 7).toInt(),
        this.startTime.substring(8, 10).toInt(),
        this.startTime.substring(11, 13).toInt(),
        this.startTime.substring(14, 16).toInt()
    ),
    endTime = LocalDateTime.of(
        this.endTime.substring(0, 4).toInt(),
        this.endTime.substring(5, 7).toInt(),
        this.endTime.substring(8, 10).toInt(),
        this.endTime.substring(11, 13).toInt(),
        this.endTime.substring(14, 16).toInt()
    ),
    phone = this.phone,
    status = Booking.Status.valueOf(this.status)
)

fun BookingDtoUpdate.toModel() = Booking(
    spaceId = this.spaceId,
    userId = this.userId,
    startTime = LocalDateTime.of(
        this.startTime.substring(0, 4).toInt(),
        this.startTime.substring(5, 7).toInt(),
        this.startTime.substring(8, 10).toInt(),
        this.startTime.substring(11, 13).toInt(),
        this.startTime.substring(14, 16).toInt()
    ),
    endTime = LocalDateTime.of(
        this.endTime.substring(0, 4).toInt(),
        this.endTime.substring(5, 7).toInt(),
        this.endTime.substring(8, 10).toInt(),
        this.endTime.substring(11, 13).toInt(),
        this.endTime.substring(14, 16).toInt()
    ),
    phone = this.phone,
    status = Booking.Status.valueOf(this.status)
)

