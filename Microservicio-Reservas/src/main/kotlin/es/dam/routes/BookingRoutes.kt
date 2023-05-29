package es.dam.routes

import es.dam.dto.BookingAllDto
import es.dam.dto.BookingDtoCreate
import es.dam.dto.BookingDtoUpdate
import es.dam.exceptions.BookingException
import es.dam.mappers.toDTO
import es.dam.mappers.toModel
import es.dam.models.Booking
import es.dam.services.BookingServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import org.litote.kmongo.id.toId
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeParseException

fun Application.bookingRoutes(){
    val bookingService: BookingServiceImpl by inject()

    routing {
        get("/bookings") {
            val data = bookingService.findAll()
            if (data.isEmpty())
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva")
            val response = BookingAllDto(data = data.map { it.toDTO()})
            call.respond(response)
        }

        get("/bookings/{id}") {
            val id = call.parameters["id"]
            try {
                id.let { bookingService.findById(it!!).let { it1 -> call.respond(it1.toDTO()) } }
            } catch (e: BookingException) {
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado la reserva con el id: $id")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

        get("/bookings/status/{status}") {
            val status = call.parameters["status"]
            try{
                val data = status?.let { bookingService.findAllStatus(Booking.Status.valueOf(it))}
                val res = BookingAllDto(data = data!!.map { it.toDTO() })
                call.respond(res)
            } catch (e: BookingException ){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el estado: $status")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "El estado debe ser un estado válido")
            }
        }

        get("/bookings/space/{id}") {
            val id = call.parameters["id"]
            try{
                val data = id?.let { bookingService.findBySpaceId(it) }
                val res = BookingAllDto(
                    data = data!!.map { it.toDTO() }
                )
                call.respond(res)
            } catch (e: BookingException ){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el id de espacio: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

        get("/bookings/user/{id}") {
            val id = call.parameters["id"]
            try {
                val data = id?.let { bookingService.findByUserId(it) }
                val res = BookingAllDto(
                    data = data!!.map { it.toDTO() }
                )
                call.respond(res)
            } catch (e: BookingException ){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el id de usuario: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

        get("/bookings/time/{id}/{date}") {
            val id = call.parameters["id"]
            val date = call.parameters["date"]
            try {
                LocalDate.parse(date)
                val data = id?.let { bookingService.findByDate(id, date!!) }
                val res = BookingAllDto(
                    data = data!!.map { it.toDTO() }
                )
                call.respond(res)
            }catch (e: BookingException) {
                    call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna sala con el uuid: $id")
            } catch (e: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            } catch (e: DateTimeParseException){
                call.respond(HttpStatusCode.BadRequest, "La fecha debe tener un formato válido")
            }
        }

        post("/bookings") {
            val booking = call.receive<BookingDtoCreate>()
            try{
                bookingService.save(booking.toModel()).let { call.respond(HttpStatusCode.Created, it.toDTO()) }
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "Deben de estar todos los campos rellenos correctamente")
            }
        }

        put("/bookings/{id}") {
            val id = call.parameters["id"]
            val booking = call.receive<BookingDtoUpdate>()
            try{
                bookingService.update(booking.toModel(), id!!).let { call.respond( it.toDTO()) }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.NotFound, "No se ha podido actualizar la reserva con id: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

        delete("/bookings/{id}") {
            val id = call.parameters["id"]
            try {
                bookingService.delete(id!!).let { call.respond(HttpStatusCode.NoContent) }
            } catch (e: BookingException) {
                call.respond(HttpStatusCode.NotFound, "No se ha podido borrar la reserva con id: $id")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }
    }
}