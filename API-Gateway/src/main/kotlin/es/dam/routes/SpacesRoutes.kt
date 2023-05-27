package es.dam.routes

import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpacePhotoDTO
import es.dam.dto.SpaceUpdateDTO
import es.dam.exceptions.*
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.services.token.TokensService
import es.dam.repositories.space.KtorFitSpacesRepository
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.ktor.ext.inject
import retrofit2.await
import java.io.File
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.UUID

private const val ENDPOINT = "spaces"

fun Application.spacesRoutes() {
    val bookingsRepository : KtorFitBookingsRepository by inject()
    val spacesRepository : KtorFitSpacesRepository by inject()
    val tokenService : TokensService by inject()

    routing {
        route("/$ENDPOINT") {
            authenticate {
                get() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)


                        val res = async {
                            spacesRepository.findAll("Bearer $token")
                        }
                        val spaces = res.await()

                        call.respond(HttpStatusCode.OK, spaces)

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }  catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/{id}") {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val space = async {
                            spacesRepository.findById("Bearer $token", id!!)
                        }

                        call.respond(HttpStatusCode.OK, space.await())

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/reservables/{isReservable}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val isReservable = call.parameters["isReservable"].toBoolean()

                        val res = async {
                            spacesRepository.findAllReservables("Bearer $token", isReservable)
                        }
                        val spaces = res.await()

                        call.respond(HttpStatusCode.OK, spaces)

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                get("/nombre/{name}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val name = call.parameters["name"]

                        val space = async {
                            spacesRepository.findByName("Bearer $token", name!!)
                        }

                        call.respond(HttpStatusCode.OK, space.await())

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                post() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val entity = call.receive<SpaceCreateDTO>()
                        val space = async { spacesRepository.create(token,  entity) }
                        call.respond(HttpStatusCode.Created, space.await())

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                post("/storage"){
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val multipart = call.receiveMultipart()
                        var spacePhotoDto: SpacePhotoDTO? = null

                        multipart.forEachPart { part ->
                            when (part) {
                                is PartData.FileItem  -> {
                                    val inputStream = part.streamProvider()
                                    val fileBytes = inputStream.readBytes()
                                    val requestBody = fileBytes.toRequestBody("image/png".toMediaTypeOrNull())
                                    val multipartBody = MultipartBody.Part.createFormData("file", "${UUID.randomUUID()}.png", requestBody)
                                    spacePhotoDto = spacesRepository.uploadFile(token, multipartBody).await()
                                }
                                else -> {
                                    throw BookingMediaNotSupportedException("Este tipo de archivo no está soportado")
                                }
                            }
                            part.dispose()
                        }
                        call.respond(HttpStatusCode.Created, spacePhotoDto!!)
                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: BookingMediaNotSupportedException) {
                        call.respond(HttpStatusCode.UnsupportedMediaType, "${e.message}")
                    }
                }



                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val space = call.receive<SpaceUpdateDTO>()

                        val updatedspace = async {
                            spacesRepository.update("Bearer $token", id!!, space)
                        }

                        call.respond(HttpStatusCode.OK, updatedspace.await())

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val uuid = UUID.fromString(id)

                        require(bookingsRepository.findBySpace("Bearer $token", id!!).data.isEmpty())
                        {"Se deben actualizar o eliminar las reservas asociadas a esta sala antes de continuar con la operación."}

                        spacesRepository.delete("Bearer $token", id!!)

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: SpaceNotFoundException) {
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: SpaceBadRequestException) {
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: SpaceInternalErrorException) {
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        call.respond(HttpStatusCode.BadRequest, "El id debe ser un id válido")
                    }
                }
            }
            get("/storage/{uuid}") {
                try {
                    val uuid = call.parameters["uuid"]

                    val spacePhotoDto = async {
                        spacesRepository.downloadFile(uuid!!)
                    }.await()
                    call.respondFile(spacePhotoDto)
                } catch (e: SpaceNotFoundException) {
                    call.respond(HttpStatusCode.NotFound, "${e.message}")
                } catch (e: SpaceBadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                } catch (e: SpaceInternalErrorException) {
                    call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                }
            }

        }
    }
}