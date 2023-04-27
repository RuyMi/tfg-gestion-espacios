package es.dam.plugins

import es.dam.routes.bookingsRoutes
import es.dam.routes.spacesRoutes
import es.dam.routes.usersRoutes
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    bookingsRoutes()
    usersRoutes()
    spacesRoutes()
}
