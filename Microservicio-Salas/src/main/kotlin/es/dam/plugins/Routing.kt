package es.dam.plugins

import es.dam.routes.spaceRoutes
import es.dam.routes.storageRoutes
import io.ktor.server.application.*

fun Application.configureRouting() {
    spaceRoutes()
    storageRoutes()
}