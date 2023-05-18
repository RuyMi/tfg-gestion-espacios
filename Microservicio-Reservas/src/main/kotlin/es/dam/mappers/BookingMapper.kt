package es.dam.mappers

import es.dam.dto.BookingDto
import es.dam.dto.BookingDtoCreate
import es.dam.dto.BookingDtoUpdate
import es.dam.models.Booking
import java.time.LocalDateTime


fun BookingDtoCreate.toModel() = Booking(
    spaceId = this.spaceId,
    spaceName = this.spaceName,
    userId = this.userId,
    userName = this.userName,
    startTime = LocalDateTime.parse(this.startTime),
    endTime = LocalDateTime.parse(this.endTime),
    phone = this.phone,
    status = Booking.Status.valueOf(this.status)
)

fun BookingDtoUpdate.toModel() = Booking(
    spaceId = this.spaceId,
    spaceName = this.spaceName,
    userId = this.userId,
    userName = this.userName,
    startTime = LocalDateTime.parse(this.startTime),
    endTime = LocalDateTime.parse(this.endTime),
    phone = this.phone,
    status = Booking.Status.valueOf(this.status)
)

fun Booking.toDTO() = BookingDto(
    id = this.id.toString(),
    uuid = this.uuid,
    spaceId = this.spaceId,
    spaceName = this.spaceName,
    userId = this.userId,
    userName = this.userName,
    startTime = this.startTime.toString(),
    endTime = this.endTime.toString(),
    phone = this.phone,
    status = this.status.toString()
)

