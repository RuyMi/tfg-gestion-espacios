package es.dam

import io.ktor.server.application.*
import es.dam.plugins.*
import io.ktor.server.netty.*
import kotlin.time.Duration

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureKoin()
    configureStorage()
    configureSerialization()
    configureRouting()
    configureValidation()
}