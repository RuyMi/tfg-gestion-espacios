package es.dam.mappers

import es.dam.dto.SpaceDto
import es.dam.models.Space
import org.litote.kmongo.toId
import java.util.*

fun Space.toSpaceDto() = SpaceDto(
    id = this.id.toString(),
    uuid = this.uuid.toString(),
    name = this.name,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    maxBookings = this.maxBookings,
    authorizedRoles = this.authorizedRoles,
    bookingWindow = this.bookingWindow
)

fun SpaceDto.toModel() = Space(
    id = this.id.toId(),
    uuid = UUID.fromString(this.uuid),
    name = this.name,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    maxBookings = this.maxBookings,
    authorizedRoles = this.authorizedRoles,
    bookingWindow = this.bookingWindow
)

