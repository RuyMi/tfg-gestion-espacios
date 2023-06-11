package es.dam

import es.dam.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

/**
 * Función principal de la aplicación. Inicia el servidor.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureStorage()
    configureSerialization()
    configureRouting()
    configureValidation()
}