package es.dam.routes

import es.dam.dto.BookingDtoCreate
import es.dam.dto.BookingDtoUpdate
import es.dam.exceptions.BookingException
import es.dam.mappers.toModel
import es.dam.models.Booking
import es.dam.services.BookingServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.koin.ktor.ext.inject
import org.litote.kmongo.id.toId
import java.time.LocalDateTime

fun Application.bookingRoutes(){
    val bookingService: BookingServiceImpl by inject()

    routing {
        get("/bookings") {
            call.respond(bookingService.findAll())
        }

        get("/bookings/{id}") {
            val id = call.parameters["id"]
            try {
                val idBooking = ObjectId(id).toId<Booking>()
                idBooking.let { bookingService.findById(it).let { it1 -> call.respond(it1) } }
            } catch (e: BookingException) {
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado la reserva con el id: $id")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound, "El id debe ser un id válido")
            }
        }

        get("/bookings/status/{status}") {
            val status = call.parameters["status"]
            try{
                status?.let { bookingService.findAllStatus(Booking.Status.valueOf(it)).let { it1 -> call.respond(it1) } }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el estado: $status")
            } catch (e: Exception){
                call.respond(HttpStatusCode.NotFound, "El estado debe ser un estado válido")
            }
        }

        get("/bookings/space/{id}") {
            val id = call.parameters["id"]
            try{
                id?.let { bookingService.findBySpaceId(it).let { it1 -> call.respond(it1) } }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el id de espacio: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.NotFound, "El id debe ser un id válido")
            }
        }

        get("/bookings/user/{id}") {
            val id = call.parameters["id"]
            try {
                id?.let { bookingService.findByUserId(it).let { it1 -> call.respond(it1) } }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el id de usuario: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.NotFound, "El id debe ser un id válido")
            }
        }

        post("/bookings") {
            val booking = call.receive<BookingDtoCreate>()
            try{
                bookingService.save(booking.toModel()).let { call.respond(it) }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.BadRequest, "No se ha podido crear la reserva")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "No se ha podido crear la reserva")
            }
        }

        put("/bookings/{id}") {
            val id = call.parameters["id"]
            val booking = call.receive<BookingDtoUpdate>()
            try{
                bookingService.update(booking.toModel(), id!!).let { call.respond(it) }
            } catch (e: BookingException){
                call.respond(HttpStatusCode.BadRequest, "No se ha podido actualizar la reserva con id: $id")
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

        delete("/bookings/{id}") {
            val id = call.parameters["id"]
            try {
                val idBooking = ObjectId(id).toId<Booking>()
                bookingService.delete(idBooking).let { call.respond(it) }
            } catch (e: BookingException) {
                call.respond(HttpStatusCode.BadRequest, "No se ha podido borrar la reserva con id: $id")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
            }
        }

    }
}