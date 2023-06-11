package es.dam.mappers

import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpaceDTO
import es.dam.dto.SpaceUpdateDTO
import es.dam.models.Space
import org.litote.kmongo.toId

/**
 * Mappers de la clase Space. Utilizados para convertir de Space a SpaceDTO y viceversa.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Space.toSpaceDto() = SpaceDTO(
    id = this.id.toString(),
    uuid = this.uuid,
    name = this.name,
    description = this.description,
    image = this.image,
    price = this.price,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    authorizedRoles = this.authorizedRoles.map { it.toString() }.toSet(),
    bookingWindow = this.bookingWindow
)

fun SpaceDTO.toModel() = Space(
    id = this.id.toId(),
    uuid = this.uuid,
    name = this.name,
    description = this.description,
    image = this.image,
    price = this.price,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    authorizedRoles = this.authorizedRoles.map { Space.UserRole.valueOf(it) }.toSet(),
    bookingWindow = this.bookingWindow
)

fun SpaceCreateDTO.toModel() = Space(
    name = this.name,
    description = this.description,
    image = this.image,
    price = this.price,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    authorizedRoles = this.authorizedRoles.map { Space.UserRole.valueOf(it) }.toSet(),
    bookingWindow = this.bookingWindow
)

fun SpaceUpdateDTO.toModel() = Space(
    name = this.name,
    description = this.description,
    image = this.image,
    price = this.price,
    isReservable = this.isReservable,
    requiresAuthorization = this.requiresAuthorization,
    authorizedRoles = this.authorizedRoles.map { Space.UserRole.valueOf(it) }.toSet(),
    bookingWindow = this.bookingWindow
)

