package es.dam.routes

import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpaceUpdateDTO
import es.dam.services.token.TokensService
import es.dam.repositories.space.KtorFitSpacesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException
import java.util.UUID

private const val ENDPOINT = "spaces"

fun Application.spacesRoutes() {
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al obtener las salas: ${e.stackTraceToString()}")
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound,"La sala con ese id no ha sido encontrada: ${e.stackTraceToString()}")
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al obtener salas reservables: ${e.stackTraceToString()}")
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound,"La sala con ese id no ha sido encontrada: ${e.stackTraceToString()}")
                    }
                }

                post() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val entity = call.receive<SpaceCreateDTO>()

                        val space = async {
                            spacesRepository.create(token,  entity)
                        }

                        call.respond(HttpStatusCode.Created, space.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Esta sala ya ha sido creada: ${e.stackTraceToString()}")
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

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar la sala: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "EError al actualizar la sala: ${e.stackTraceToString()}")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val uuid = UUID.fromString(id)

                        spacesRepository.delete("Bearer $token", id!!)

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: IllegalArgumentException) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "El id introducido no es válido: ${e.stackTraceToString()}"
                        )
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.NotFound,
                            "La sala con ese id no ha sido encontrada: ${e.stackTraceToString()}"
                        )
                    }
                }
            }
        }
    }
}