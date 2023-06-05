package es.dam

import es.dam.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureStorage()
    configureSerialization()
    configureRouting()
    configureValidation()
}