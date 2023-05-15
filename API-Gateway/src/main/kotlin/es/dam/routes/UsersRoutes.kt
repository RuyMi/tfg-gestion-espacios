package es.dam.routes

import es.dam.dto.UserLoginDTO
import es.dam.dto.UserRegisterDTO
import es.dam.services.token.TokensService
import es.dam.dto.UserUpdateDTO
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.user.KtorFitUsersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jdk.jshell.spi.ExecutionControl.UserException
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

private const val ENDPOINT = "users"

fun Application.usersRoutes() {
    val bookingsRepository : KtorFitBookingsRepository by inject()
    val userRepository : KtorFitUsersRepository by inject()
    val tokenService : TokensService by inject()

    routing {

        route("/$ENDPOINT") {

            post("/login") {
                try {
                    val login = call.receive<UserLoginDTO>()

                    require(userRepository.isActive(login.username)){"No se ha podido iniciar sesión ya que este usuario está dado de baja."}
                    val user = async {
                        userRepository.login(login)
                    }

                    call.respond(HttpStatusCode.OK, user.await())

                } catch (e: UserException) {
                    call.respond(HttpStatusCode.BadRequest, "Usuario o contraseña inválidos: ${e.stackTraceToString()}")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error al hacer login: ${e.stackTraceToString()}")
                }
            }

            post("/register") {
                try {
                    val register = call.receive<UserRegisterDTO>()

                    val user = async {
                        userRepository.register(register)
                    }

                    call.respond(HttpStatusCode.Created, user.await())

                } catch (e: UserException) {
                    call.respond(HttpStatusCode.BadRequest, "El usuario ya esta registrado: ${e.stackTraceToString()}")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Error al registrar el usuario: ${e.stackTraceToString()}")
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
                        call.respond(HttpStatusCode.NotFound, "Error getting users")
                    }
                }

                get("/{id}") {

                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        val user = async {
                            userRepository.findById("Bearer $token", id!!)
                        }

                        call.respond(HttpStatusCode.OK, user.await())

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound,"El usuario con ese id no ha sido encontrado: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al obtener el usuario: ${e.stackTraceToString()}")
                    }
                }

                put("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val user = call.receive<UserUpdateDTO>()

                        val updatedUser = async {
                            userRepository.update("Bearer $token", id!!, user)
                        }

                        call.respond(HttpStatusCode.OK, updatedUser.await())

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    }
                }

                put("/me") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val user = call.receive<UserUpdateDTO>()

                        val updatedUser = async {
                            userRepository.me("Bearer $token", user)
                        }

                        call.respond(HttpStatusCode.OK, updatedUser.await())

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    }
                }

                put("/credits/{id}/{creditsAmount}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val creditsAmount = call.parameters["creditsAmount"]

                        val updatedUser = async {
                            userRepository.updateCredits("Bearer $token", id!!, creditsAmount!!.toInt())
                        }

                        call.respond(HttpStatusCode.OK, updatedUser.await())

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound, "El usuario con ese id no ha sido encontrado: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    }
                }

                put("/active/{id}/{active}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]
                        val active = call.parameters["active"]

                        val updatedUser = async {
                            userRepository.updateActive("Bearer $token", id!!, active!!.toBoolean())
                        }
                        call.respond(HttpStatusCode.OK, updatedUser.await())

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound, "El usuario con ese id no ha sido encontrado: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario: ${e.stackTraceToString()}")
                    }
                }

                delete("/{id}") {
                    try {
                        val token = tokenService.generateToken(call.principal()!!)
                        val id = call.parameters["id"]

                        require(bookingsRepository.findByUser(token, id!!).data.isNotEmpty())
                        {"Se deben actualizar o eliminar las reservas asociadas a este usuario antes de continuar con la operación."}
                        userRepository.delete("Bearer $token", id!!)

                        call.respond(HttpStatusCode.NoContent)

                    } catch (e: UserException) {
                        call.respond(HttpStatusCode.NotFound, "El usuario con ese id no ha sido encontrado: ${e.stackTraceToString()}")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Error al eliminar el usuario: ${e.stackTraceToString()}")
                    }
                }
            }
        }
    }
}