package es.dam.routes

import es.dam.dto.BookingCreateDTO
import es.dam.services.token.TokensService
import es.dam.dto.BookingUpdateDTO
import es.dam.repositories.booking.KtorFitBookingsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject

private const val ENDPOINT = "bookings"

fun Application.bookingsRoutes() {
    val bookingsRepository : KtorFitBookingsRepository by inject()
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al obtener las reservas")
                    }
                }

                get("/{id}") {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val booking = async {
                            bookingsRepository.findById("Bearer $token", id!!.toLong())
                        }

                        call.respond(HttpStatusCode.OK, booking.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "La reserva con ese id no ha sido encontrada")
                    }
                }

                get("/space/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val res = async {
                            bookingsRepository.findBySpace("Bearer $token", id!!.toLong())
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al encontrar las reservas para ese id de sala")
                    }
                }

                get("/user/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val res = async {
                            bookingsRepository.findByUser("Bearer $token", id!!.toLong())
                        }
                        val bookings = res.await()

                        call.respond(HttpStatusCode.OK, bookings)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al encontrar las reservas para ese id de sala")
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al encontrar la reserva con ese estado")
                    }
                }

                post() {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val entity = call.receive<BookingCreateDTO>()

                        val booking = async {
                            bookingsRepository.create(token, entity)
                        }

                        call.respond(HttpStatusCode.Created, booking.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "La reserva ya ha sido creada")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val booking = call.receive<BookingUpdateDTO>()

                        val updatedbooking = async {
                            bookingsRepository.update("Bearer $token", id!!.toLong(), booking)
                        }

                        call.respond(HttpStatusCode.OK, updatedbooking.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar la reserva")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar la reserva")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        bookingsRepository.delete("Bearer $token", id!!.toLong())

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "La reserva con ese id no ha sido encontrada")
                    }
                }
            }
        }
    }
}