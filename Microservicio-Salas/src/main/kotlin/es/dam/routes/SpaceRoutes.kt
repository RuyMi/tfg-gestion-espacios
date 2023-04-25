package es.dam.routes

import es.dam.models.Space
import es.dam.services.SpaceServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
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

        get("/spaces/reservables/{isReservable}") {
            val isReservable = call.parameters["isReservable"]
            isReservable?.let { spaceService.getAllSpacesReservables(it.toBoolean()).let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado ningún espacio reservable = $isReservable")
        }

        get("/spaces/nombre/{name}") {
            val name = call.parameters["name"]
            name?.let { spaceService.getSpaceByName(it).let { it1 -> call.respond(it1!!) } }
            call.respond(HttpStatusCode.NotFound, "No se ha encontrado el espacio con el nombre: $name")
        }

        post("/spaces") {
            val space = call.receive<Space>()
            spaceService.createSpace(space)?.let { call.respond(it) }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido crear el espacio")
        }

        put("/spaces") {
            val space = call.receive<Space>()
            spaceService.updateSpace(space)?.let { call.respond(it) }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido actualizar el espacio")
        }

        delete("/spaces") {
            val id = call.parameters["id"]
            id?.let { spaceService.deleteSpace(it).let { it1 -> call.respond(it1) } }
            call.respond(HttpStatusCode.BadRequest, "No se ha podido borrar el espacio")
        }
    }
}