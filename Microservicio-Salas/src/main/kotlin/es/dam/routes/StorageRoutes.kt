package es.dam.routes

import es.dam.services.storage.StorageService
import es.dam.services.storage.StorageServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import java.util.*

fun Application.storageRoutes() {
    val storageService: StorageServiceImpl by inject(named("StorageServiceImpl"))

    routing {
        route("/spaces/storage") {
            post {
                val readChannel = call.receiveChannel()
                val fileName = UUID.randomUUID().toString()
                val res = storageService.saveFile(fileName, readChannel)
                call.respond(HttpStatusCode.OK, res)
            }

            get("{fileName}") {
                val fileName = call.parameters["fileName"].toString()
                val file = storageService.getFile(fileName)
                call.respondFile(file)
            }

            delete("{fileName}") {
                val fileName = call.parameters["fileName"].toString()
                storageService.deleteFile(fileName)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}