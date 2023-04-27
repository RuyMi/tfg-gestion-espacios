package es.dam.routes

import es.dam.dto.UserLoginDTO
import es.dam.dto.UserRegisterDTO
import es.dam.services.token.TokensService
import es.dam.dto.UserUpdateDTO
import es.dam.repositories.user.KtorFitUsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject

private const val ENDPOINT = "users"

fun Application.usersRoutes() {
    val userRepository : KtorFitUsersRepository by inject()
    val tokenService : TokensService by inject()

    routing {

        route("/$ENDPOINT") {

            post("/login") {
                try {
                    val login = call.receive<UserLoginDTO>()

                    val user = async {
                        userRepository.login(login)
                    }

                    call.respond(HttpStatusCode.OK, user.await())

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Usuario o contraseña inválidos.")
                }
            }

            post("/register") {
                try {
                    val register = call.receive<UserRegisterDTO>()

                    val user = async {
                        userRepository.register(register)
                    }

                    call.respond(HttpStatusCode.Created, user.await())

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "El usuario ya esta registrado")
                }
            }

            authenticate {
                get() {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)

                        val res = async {
                            userRepository.findAll("Bearer $token")
                        }
                        val users = res.await()

                        call.respond(HttpStatusCode.OK, users)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al obtener los usuarios")
                    }
                }

                get("/{id}") {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val user = async {
                            userRepository.findById("Bearer $token", id!!.toLong())
                        }

                        call.respond(HttpStatusCode.OK, user.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound,"El usuario con ese id no ha sido encontrado")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val user = call.receive<UserUpdateDTO>()

                        val updatedUser = async {
                            userRepository.update("Bearer $token", id!!.toLong(), user)
                        }

                        call.respond(HttpStatusCode.OK, updatedUser.await())

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar el usuario")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        userRepository.delete("Bearer $token", id!!.toLong())

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.NotFound, "El usuario con ese id no ha sido encontrado")
                    }
                }
            }
        }
    }
}