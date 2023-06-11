package es.dam.plugins

import es.dam.routes.bookingsRoutes
import es.dam.routes.spacesRoutes
import es.dam.routes.usersRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Función que configura las rutas de la aplicación.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Accede a la web de la aplicación en: http://app.iesluisvives.org:2121/")
        }
    }

    bookingsRoutes()
    usersRoutes()
    spacesRoutes()
}
