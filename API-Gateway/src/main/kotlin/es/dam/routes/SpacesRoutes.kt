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
                        call.respond(HttpStatusCode.NotFound, "Error al obtener las salas")
                    }
                }

                get("/{id}") {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val space = async {
                            spacesRepository.findById("Bearer $token", id!!.toLong())
                        }

                        call.respond(HttpStatusCode.OK, space.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound,"La sala con ese id no ha sido encontrada")
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
                        call.respond(HttpStatusCode.NotFound, "Error al obtener salas reservables")
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
                        call.respond(HttpStatusCode.NotFound,"La sala con ese id no ha sido encontrada")
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
                        call.respond(HttpStatusCode.BadRequest, "Esta sala ya ha sido creada")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val space = call.receive<SpaceUpdateDTO>()

                        val updatedspace = async {
                            spacesRepository.update("Bearer $token", id!!.toLong(), space)
                        }

                        call.respond(HttpStatusCode.OK, updatedspace.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar la sala")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "EError al actualizar la sala")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        spacesRepository.delete("Bearer $token", id!!.toLong())

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "La sala con ese id no ha sido encontrada")
                    }
                }
            }
        }
    }
}