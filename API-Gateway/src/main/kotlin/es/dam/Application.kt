package es.dam

import es.dam.plugins.*
import io.ktor.server.application.*

/**
 * Función main del microservicio. Se encarga de ejecutar la aplicación.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureKoin()
    configureSecurity()
    configureCors()
    configureSerialization()
    configureHTTP()
    configureSwagger()
    configureRouting()
}
