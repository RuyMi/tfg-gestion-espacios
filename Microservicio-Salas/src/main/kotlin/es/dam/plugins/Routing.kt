package es.dam.plugins

import es.dam.routes.spaceRoutes
import es.dam.routes.storageRoutes
import io.ktor.server.application.*

/**
 * Configuración de las rutas del microservicio
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Application.configureRouting() {
    spaceRoutes()
    storageRoutes()
}