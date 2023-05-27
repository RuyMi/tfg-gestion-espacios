package es.dam.routes

import es.dam.dto.BookingCreateDTO
import es.dam.dto.BookingResponseDTO
import es.dam.services.token.TokensService
import es.dam.dto.BookingUpdateDTO
import es.dam.exceptions.BookingBadRequestException
import es.dam.exceptions.BookingExceptions
import es.dam.exceptions.BookingInternalErrorException
import es.dam.exceptions.BookingNotFoundException
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.space.KtorFitSpacesRepository
import es.dam.repositories.user.KtorFitUsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeParseException
import java.util.*

private const val ENDPOINT = "bookings"

fun Application.bookingsRoutes() {
    val bookingsRepository : KtorFitBookingsRepository by inject()
    val userRepository : KtorFitUsersRepository by inject()
    val spaceRepository : KtorFitSpacesRepository by inject()
    val tokenService : TokensService by inject()

    routing {
        route("/$ENDPOINT") {
            authenticate {
                get() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)

                        val res = async {
                            bookingsRepository.findAll("Bearer $token")
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val booking = async {
                            bookingsRepository.findById("Bearer $token", id!!)
                        }

                        call.respond(HttpStatusCode.OK, booking.await())

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/space/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val res = async {
                            bookingsRepository.findBySpace("Bearer $token", id!!)
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/user/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val res = async {
                            bookingsRepository.findByUser("Bearer $token", id!!)
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/status/{status}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val status = call.parameters["status"]

                        val res = async {
                            bookingsRepository.findByStatus("Bearer $token", status!!)
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/time/{idSpace}/{date}"){
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["idSpace"]
                        val date = call.parameters["date"]
                        val res = async {
                            bookingsRepository.findByTime("Bearer $token", id!!, date!!)
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                post() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        var entity = call.receive<BookingCreateDTO>()
                        val user = userRepository.findById("Bearer $token", entity.userId)
                        val space = spaceRepository.findById("Bearer $token", entity.spaceId)

                        if(!user.userRole.contains("ADMINISTRATOR")) {
                            if (user.credits < space.price) {
                                call.respond(
                                    HttpStatusCode.BadRequest,
                                    "No tienes créditos suficientes para realizar la reserva"
                                )
                            }
                        }
                        userRepository.updateCredits("Bearer $token", user.uuid, space.price)
                        require(LocalDateTime.parse(entity.startTime) > LocalDateTime.now())
                        {"No se ha podio guardar la reserva fecha introducida es anterior a la actual."}
                        require(Period.between(LocalDate.now(),LocalDate.parse(entity.startTime.split("T")[0])).days <= space.bookingWindow.toInt())
                        {"No se puede reservar con tanta anterioridad."}
                        require(bookingsRepository.findByTime("Bearer $token", entity.spaceId, entity.startTime.split("T")[0])
                            .data
                            .filter{it -> it.startTime.split("T")[1].split(":")[0] == entity.startTime.split("T")[1].split(":")[0]}
                            .isEmpty()
                        )
                        {"Franja horaria no disponible."}

                        entity = entity.copy(spaceName = space.name, userName = user.name, image = space.image)
                        val booking: Deferred<BookingResponseDTO> = if(!space.requiresAuthorization){
                            async {
                                bookingsRepository.create("Bearer $token", entity.copy(status = "APPROVED"))
                            }
                        }else{
                            async {
                                bookingsRepository.create("Bearer $token", entity)
                            }
                        }

                        call.respond(HttpStatusCode.Created, booking.await())
                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val booking = call.receive<BookingUpdateDTO>()

                        require(userRepository.findById("Bearer $token", bookingsRepository.findById("Bearer $token", id!!).userId).userRole.contains("ADMINISTRATOR") ||bookingsRepository.findByUser("Bearer $token", booking.userId).data.filter{it.userId == booking.userId}.isNotEmpty())
                        {"La reserva que se quiere actualizar no está guardada bajo el mismo usuario."}
                        require(LocalDateTime.parse(booking.startTime) > LocalDateTime.now())
                        {"No se ha podio guardar la reserva fecha introducida es anterior a la actual."}
                        require(Period.between(LocalDate.now(),LocalDate.parse(booking.startTime.split("T")[0])).days <=
                                spaceRepository.findById("Bearer $token", booking.spaceId).bookingWindow.toInt())
                        {"No se puede reservar con tanta anterioridad."}
                        require(bookingsRepository.findByTime(
                            "Bearer $token",
                            booking.spaceId,
                            booking.startTime.split("T")[0]
                        )
                            .data
                            .filter { it.startTime == booking.startTime }.none { it.uuid != id }
                        )
                        {"Franja horaria no disponible."}

                        val updatedbooking: Deferred<BookingResponseDTO>

                        if(spaceRepository.findById(token, booking.spaceId).requiresAuthorization){
                            updatedbooking = async {
                                bookingsRepository.update("Bearer $token", id!!, booking.copy(status = "APPROVED"))
                            }
                        }else{
                            updatedbooking = async {
                                bookingsRepository.update("Bearer $token", id!!, booking)
                            }
                        }

                        val updated = updatedbooking.await()
                        call.respond(HttpStatusCode.OK, updated)

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: DateTimeParseException){
                        call.respond(HttpStatusCode.BadRequest, "Formato de fecha incorrecto.")
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    }
                }

                delete("/{id}/{userId}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val userId = call.parameters["userId"]
                        val uuid = UUID.fromString(id)!!
                        val userUuid = UUID.fromString(userId)!!

                        require(userRepository.findById("Bearer $token", bookingsRepository.findById("Bearer $token", id!!).userId).userRole.contains("ADMINISTRATOR") || bookingsRepository.findById("Bearer $token", id!!).userId == userId)
                        {"La reserva que se quiere actualizar no está guardada bajo el mismo usuario."}

                        val spaceId = bookingsRepository.findById("Bearer $token", id!!).spaceId
                        bookingsRepository.delete("Bearer $token", id!!)

                        userRepository.updateCredits(
                            "Bearer $token",
                            userId!!,
                            spaceRepository.findById(
                                "Bearer $token",
                                spaceId
                            ).price * -1
                        )

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "El id introducido no es válido: ${e.message}"
                        )
                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }
            }
        }
    }
}