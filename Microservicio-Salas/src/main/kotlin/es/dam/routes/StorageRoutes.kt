package es.dam.routes

import es.dam.dto.SpacePhotoDTO
import es.dam.services.storage.StorageServiceImpl
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.storageRoutes() {
    val storageService: StorageServiceImpl by inject()

    routing {

        route("/spaces/storage") {
            post {
                val readChannel = call.receiveMultipart()
                var fileName: String? = null
                var fileBytes: ByteArray? = null
                readChannel.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            fileBytes = part.streamProvider().use { it.readBytes() }
                            fileName = part.originalFileName
                        }
                        is PartData.FormItem -> {
                        }
                        else -> {}
                    }
                    part.dispose()
                }

                val res = storageService.saveFile(fileName!!, fileBytes!!)
                val data = SpacePhotoDTO(res)
                call.respond(HttpStatusCode.OK, data)
            }

            get("{fileName}") {
                val fileName = call.parameters["fileName"].toString()
                try{
                    val file = storageService.getFile(fileName)
                    call.respondFile(file)
                }catch (e: Exception){
                    call.respond(HttpStatusCode.NotFound, "No se ha encontrado el archivo con el nombre: $fileName")
                }
            }

            delete("{fileName}") {
                val fileName = call.parameters["fileName"].toString()
                storageService.deleteFile(fileName)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}