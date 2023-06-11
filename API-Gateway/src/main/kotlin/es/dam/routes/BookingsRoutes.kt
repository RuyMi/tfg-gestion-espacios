package es.dam.routes

import es.dam.dto.BookingCreateDTO
import es.dam.dto.BookingDataDTO
import es.dam.dto.BookingResponseDTO
import es.dam.dto.BookingUpdateDTO
import es.dam.exceptions.*
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.space.KtorFitSpacesRepository
import es.dam.repositories.user.KtorFitUsersRepository
import es.dam.services.token.TokensService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*

private const val ENDPOINT = "bookings"

/**
 * Clase que contiene las rutas de la entidad Booking
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

fun Application.bookingsRoutes() {
    val bookingsRepository : KtorFitBookingsRepository by inject()
    val userRepository : KtorFitUsersRepository by inject()
    val spaceRepository : KtorFitSpacesRepository by inject()
    val tokenService : TokensService by inject()

    routing {
        route("/$ENDPOINT") {
            authenticate {
                get {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}

                        val res = runCatching {
                            bookingsRepository.findAll("Bearer $token")
                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }
                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException){
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                get("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val id = call.parameters["id"]

                        require(userRole.contains( "ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}

                        try{
                            UUID.fromString(id)
                        } catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@get
                        }

                        val bookingResult = runCatching {
                            bookingsRepository.findById("Bearer $token", id!!)
                        }

                        if (bookingResult.isSuccess) {
                            call.respond(HttpStatusCode.OK, bookingResult.getOrNull()!!)
                        } else {
                            throw bookingResult.exceptionOrNull()!!
                        }

                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
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
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        val id = call.parameters["id"]

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}

                        try{
                            UUID.fromString(id)
                        }catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@get
                        }

                        val res = runCatching {
                            bookingsRepository.findBySpace("Bearer $token", id!!)
                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }

                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
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
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val subject = originalToken.payload.subject
                        val id = call.parameters["id"]

                        require(userRole.contains("ADMINISTRATOR") || id == subject){"Esta operación no está permitida para los usuarios que no son administradores."}

                        try {
                            UUID.fromString(id)
                        }catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@get
                        }

                        val res = runCatching {
                            bookingsRepository.findByUser("Bearer $token", id!!)
                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }

                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/not-finished/user/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val subject = originalToken.payload.subject
                        val id = call.parameters["id"]

                        require(userRole.contains("ADMINISTRATOR") || id == subject){"Esta operación no está permitida para los usuarios que no son administradores."}

                        try {
                            UUID.fromString(id)
                        }catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@get
                        }

                        val res = runCatching {
                            BookingDataDTO(data = bookingsRepository.findByUser("Bearer $token", id!!).data.filter { LocalDateTime.parse(it.startTime).isAfter(LocalDateTime.now()) })

                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }

                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
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
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}

                        val status = call.parameters["status"]

                        if(status != "APPROVED" && status != "PENDING" && status != "REJECTED"){
                            throw BookingBadRequestException("El estado debe ser un estado válido")
                        }

                        val res = runCatching {
                            bookingsRepository.findByStatus("Bearer $token", status)
                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }

                    } catch (e: BookingNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                get("/time/{idSpace}/{date}"){
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["idSpace"]
                        UUID.fromString(id)
                        val date = call.parameters["date"]

                        val res = runCatching {
                            bookingsRepository.findByTime("Bearer $token", id!!, date!!)
                        }

                        if (res.isSuccess) {
                            call.respond(HttpStatusCode.OK, res.getOrNull()!!)
                        } else {
                            throw res.exceptionOrNull()!!
                        }
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                    } catch (e: BookingNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                post {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val subject = originalToken.payload.subject

                        val entity = call.receive<BookingCreateDTO>()
                        val user = userRepository.findById("Bearer $token", subject)
                        val space = spaceRepository.findById("Bearer $token", entity.spaceId)

                        if(!userRole.contains("ADMINISTRATOR")){
                            require(subject == entity.userId){"No se puede realizar la reserva a nombre de otra persona"}
                            require(user.credits >= space.price) {"No tienes créditos suficientes para realizar la reserva"}
                            userRepository.updateCreditsMe("Bearer $token", subject, space.price)

                            require(LocalDateTime.parse(entity.startTime).isAfter( LocalDateTime.now()))
                            {"No se ha podido guardar la reserva fecha introducida es anterior a la actual."}
                            require(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(entity.startTime.split("T")[0])) <= space.bookingWindow)
                            {"No se puede reservar con tanta anterioridad."}
                            require(bookingsRepository.findByTime("Bearer $token", entity.spaceId, entity.startTime.split("T")[0])
                                .data
                                .filter{it -> it.startTime.split("T")[1].split(":")[0] == entity.startTime.split("T")[1].split(":")[0]}
                                .isEmpty()
                            )
                            {"Franja horaria no disponible."}

                            val booking: Result<BookingResponseDTO> = if(!space.requiresAuthorization){
                                runCatching {
                                    bookingsRepository.create("Bearer $token", entity.copy(status = "APPROVED"))
                                }
                            }else{
                                runCatching {
                                    bookingsRepository.create("Bearer $token", entity)
                                }
                            }

                            if (booking.isSuccess) {
                                call.respond(HttpStatusCode.Created, booking.getOrNull()!!)
                            } else {
                                throw booking.exceptionOrNull()!!
                            }

                        }else{
                            require(LocalDateTime.parse(entity.startTime).isAfter( LocalDateTime.now()))
                            {"No se ha podido guardar la reserva fecha introducida es anterior a la actual."}
                            require(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(entity.startTime.split("T")[0])) <= space.bookingWindow)
                            {"No se puede reservar con tanta anterioridad."}
                            require(bookingsRepository.findByTime("Bearer $token", entity.spaceId, entity.startTime.split("T")[0])
                                .data
                                .filter{it -> it.startTime.split("T")[1].split(":")[0] == entity.startTime.split("T")[1].split(":")[0]}
                                .isEmpty()
                            )
                            {"Franja horaria no disponible."}

                            val booking = runCatching {
                                bookingsRepository.create("Bearer $token", entity.copy(status = "APPROVED"))
                            }

                            if (booking.isSuccess) {
                                call.respond(HttpStatusCode.Created, booking.getOrNull()!!)
                            } else {
                                throw booking.exceptionOrNull()!!
                            }
                        }

                    } catch (e: BookingNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    }catch (e: SpaceNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }
                }

                put("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val subject = originalToken.payload.subject

                        val id = call.parameters["id"]

                        try {
                            UUID.fromString(id)
                        }catch (e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@put
                        }

                        val booking = call.receive<BookingUpdateDTO>()

                        if(!userRole.contains("ADMINISTRATOR")){
                            require(bookingsRepository.findByUser("Bearer $token", booking.userId).data.filter{it.userId == subject}.isNotEmpty()){"La reserva que se quiere actualizar no está guardada bajo el mismo usuario."}
                            require(LocalDateTime.parse(bookingsRepository.findById("Bearer $token", id!!).startTime).isAfter(LocalDateTime.now()))
                            {"No se puede actualizar una reserva que ya ha comenzado."}
                            require(bookingsRepository.findById("Bearer $token", id!!).status != "REJECTED"){"No se puede actualizar una reserva rechazada."}
                            require(LocalDateTime.parse(booking.startTime).isAfter(LocalDateTime.now()))
                            {"No se ha podido guardar la reserva fecha introducida es anterior a la actual."}
                            require(Period.between(LocalDate.now(),LocalDate.parse(booking.startTime.split("T")[0])).days <=
                                    spaceRepository.findById("Bearer $token", booking.spaceId).bookingWindow.toInt())
                            {"No se puede reservar con tanta anterioridad."}
                            require(bookingsRepository.findByTime("Bearer $token", booking.spaceId, booking.startTime.split("T")[0])
                                .data
                                .filter { it.startTime == booking.startTime }.none { it.uuid != id }
                            )
                            {"Franja horaria no disponible."}

                            val updatingResult: Result<BookingResponseDTO> = runCatching {
                                bookingsRepository.update("Bearer $token", id, booking)
                            }

                            if (updatingResult.isSuccess) {
                                call.respond(HttpStatusCode.OK, updatingResult.getOrNull()!!)
                            } else {
                                throw updatingResult.exceptionOrNull()!!
                            }

                        }else{
                            require(LocalDateTime.parse(booking.startTime) > LocalDateTime.now())
                            {"No se ha podido guardar la reserva fecha introducida es anterior a la actual."}
                            require(Period.between(LocalDate.now(),LocalDate.parse(booking.startTime.split("T")[0])).days <=
                                    spaceRepository.findById("Bearer $token", booking.spaceId).bookingWindow.toInt())
                            {"No se puede reservar con tanta anterioridad."}
                            require(bookingsRepository.findByTime("Bearer $token", booking.spaceId, booking.startTime.split("T")[0])
                                .data
                                .filter{it.startTime == booking.startTime}
                                .none { it.uuid != id }
                            )
                            {"Franja horaria no disponible."}

                            val updatingResult = runCatching {
                                    bookingsRepository.update("Bearer $token", id!!, booking)
                            }

                            if (updatingResult.isSuccess) {
                                call.respond(HttpStatusCode.OK, updatingResult.getOrNull()!!)
                            } else {
                                throw updatingResult.exceptionOrNull()!!
                            }
                        }

                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }catch (e: SpaceNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }
                }

                delete("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val subject = originalToken.payload.subject
                        val id = call.parameters["id"]

                        try {
                            UUID.fromString(id)
                        }catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest,"El id introducido no es válido: ${e.message}")
                            return@delete
                        }

                        if(!userRole.contains("ADMINISTRATOR")){
                            require(bookingsRepository.findById("Bearer $token", id!!).userId == subject)
                            {"La reserva que se quiere eliminar no está guardada bajo el mismo usuario."}

                            require(LocalDateTime.parse(bookingsRepository.findById("Bearer $token", id!!).startTime).isAfter(LocalDateTime.now()))
                            {"No se puede eliminar una reserva que ya ha comenzado."}

                            val spaceId = bookingsRepository.findById("Bearer $token", id).spaceId

                            userRepository.updateCreditsMe(
                                "Bearer $token",
                                subject,
                                spaceRepository.findById(
                                    "Bearer $token",
                                    spaceId
                                ).price * -1
                            )

                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            bookingsRepository.findById("Bearer $token", id!!).spaceId
                            bookingsRepository.delete("Bearer $token", id)

                            call.respond(HttpStatusCode.NoContent)
                        }

                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: BookingBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: BookingInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }catch (e: SpaceNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }
                }
            }
        }
    }
}