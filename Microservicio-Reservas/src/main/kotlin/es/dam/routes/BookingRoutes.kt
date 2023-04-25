package es.dam.routes

import es.dam.models.Booking
import es.dam.services.BookingServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import org.litote.kmongo.id.toId

fun Application.bookingRoutes(){
    //TODO Inyeccion de dependencias
    val bookingService = BookingServiceImpl()

    routing {
        get("/bookings") {
            call.respond(bookingService.findAll())
        }

        get("/bookings/{id}") {
            val id = call.parameters["id"]
            val idBooking = ObjectId(id).toId<Booking>()
            idBooking.let { bookingService.findById(it)?.let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado la reserva con el id: $id")
        }

        get("/bookings/status/{status}") {
            val status = call.parameters["status"]
            status?.let { bookingService.findAllStatus(Booking.Status.valueOf(it)).let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el estado: $status")
        }

        get("/bookings/space/{id}") {
            val id = call.parameters["id"]
            id?.let { bookingService.findBySpaceId(it).let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con el id de espacio: $id")
        }

        get("/bookings/user/{id}") {
            val id = call.parameters["id"]
            id?.let { bookingService.findByUserId(it).let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado ninguna reserva con ese id de usuario: $id")
        }

        post("/bookings") {
            val booking = call.receive<Booking>()
            bookingService.save(booking)?.let { call.respond(it) }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido crear la reserva")
        }

        put("/bookings") {
            val booking = call.receive<Booking>()
            bookingService.update(booking)?.let { call.respond(it) }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido actualizar la reserva")
        }

        delete("/bookings/{id}") {
            val id = call.parameters["id"]
            val idBooking = ObjectId(id).toId<Booking>()
            bookingService.delete(idBooking).let { call.respond(it) }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido borrar la reserva con id: $id")
        }

    }
}