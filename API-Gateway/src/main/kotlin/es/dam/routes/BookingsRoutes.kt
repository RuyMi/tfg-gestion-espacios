package es.dam.routes

import es.dam.dto.BookingCreateDTO
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
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
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
                        val entity = call.receive<BookingCreateDTO>()
                        val user = userRepository.findById("Bearer $token", entity.userId)
                        val space = spaceRepository.findById("Bearer $token", entity.spaceId)
                        if (user.credits < space.price) {
                            call.respond(HttpStatusCode.BadRequest, "No tienes créditos suficientes para realizar la reserva")
                        }
                        userRepository.updateCredits("Bearer $token", user.uuid, space.price)
                        require(LocalDateTime.parse(entity.startTime) > LocalDateTime.now())
                        {"No se ha podio guardar la reserva fecha introducida es anterior a la actual."}
                        require(Period.between(LocalDate.now(),LocalDate.parse(entity.startTime.split("T")[0])).days <= space.bookingWindow.toInt())
                        {"No se puede reservar con tanta anterioridad."}
                        require(bookingsRepository.findByTime("Bearer $token", entity.spaceId, entity.startTime.split("T")[0])
                                .data
                                .filter{it -> it.startTime == entity.startTime}
                                .isEmpty()
                        )
                        {"Franja horaria no disponible."}

                        var booking = async {  }

                        if(!space.requiresAuthorization){
                            booking = async {
                                bookingsRepository.create(token, entity.copy(status = "APPROVED"))
                            }
                        }else{
                            booking = async {
                                bookingsRepository.create(token, entity)
                            }
                        }

                        call.respond(HttpStatusCode.Created, booking.await())
                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val booking = call.receive<BookingUpdateDTO>()

                        require(bookingsRepository.findByUser(token, booking.userId).data.filter{it.userId == booking.userId}.isEmpty())
                        {"La reserva que se quiere actualizar no está guardada bajo el mismo usuario."}
                        require(LocalDateTime.parse(booking.startTime) > LocalDateTime.now())
                        {"No se ha podio guardar la reserva fecha introducida es anterior a la actual."}
                        require(Period.between(LocalDate.now(),LocalDate.parse(booking.startTime.split("T")[0])).days <=
                                spaceRepository.findById(token, booking.spaceId).bookingWindow.toInt())
                        {"No se puede reservar con tanta anterioridad."}
                        require(bookingsRepository.findByTime(token, booking.spaceId, booking.startTime.split("T")[0])
                                .data
                                .filter{it.startTime == booking.startTime}
                                .isNotEmpty()
                        )
                        {"Franja horaria no disponible."}

                        var updatedbooking = async {}

                        if(spaceRepository.findById(token, booking.spaceId).requiresAuthorization){
                            updatedbooking = async {
                                bookingsRepository.update("Bearer $token", id!!, booking.copy(status = "APPROVED"))
                            }
                        }else{
                            updatedbooking = async {
                                bookingsRepository.update("Bearer $token", id!!, booking)
                            }
                        }

                        call.respond(HttpStatusCode.OK, updatedbooking.await())

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                delete("/{id}/{userId}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val userId = call.parameters["userId"]
                        val uuid = UUID.fromString(id)!!
                        val userUuid = UUID.fromString(userId)!!

                        require(bookingsRepository.findById(token, id!!).userId == userId)
                        {"La reserva que se quiere actualizar no está guardada bajo su mismo teléfono de contacto."}

                        bookingsRepository.delete("Bearer $token", id!!)

                        userRepository.updateCredits(
                                token,
                                userId,
                                spaceRepository.findById(
                                        token,
                                        bookingsRepository.findById(token, id!!).spaceId
                                ).price * -1
                        )

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "El id introducido no es válido: ${e.stackTraceToString()}"
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