package es.dam

import es.dam.plugins.configureKoin
import es.dam.plugins.configureRouting
import es.dam.plugins.configureSerialization
import es.dam.plugins.configureValidation
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureKoin()
    configureSerialization()
    configureRouting()
    configureValidation()
}
