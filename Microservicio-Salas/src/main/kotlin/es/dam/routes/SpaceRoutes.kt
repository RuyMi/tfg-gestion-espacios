package es.dam.routes

import es.dam.services.SpaceServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.spaceRoutes() {

    val spaceService = SpaceServiceImpl()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/spaces") {
            call.respond(spaceService.getAllSpaces())
        }

        get("/spaces/{id}") {
            val id = call.parameters["id"]
            id?.let { spaceService.getSpaceById(it)?.let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado el espacio con ese id")
        }

    }
}