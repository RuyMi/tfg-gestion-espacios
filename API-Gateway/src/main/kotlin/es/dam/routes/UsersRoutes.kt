package es.dam.routes

import es.dam.dto.UserLoginDTO
import es.dam.dto.UserPhotoDTO
import es.dam.dto.UserRegisterDTO
import es.dam.dto.UserUpdateDTO
import es.dam.exceptions.*
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.user.KtorFitUsersRepository
import es.dam.services.token.TokensService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.core.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.ktor.ext.inject
import retrofit2.await
import java.net.ConnectException
import java.util.*

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

                    val user = runCatching {
                        userRepository.login(login)
                    }

                    if (user.isSuccess) {
                        require(userRepository.isActive(login.username)){"Este usuario ha sido dado de baja."}
                        call.respond(HttpStatusCode.OK, user.getOrNull()!!)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, user.exceptionOrNull()!!.message!!)
                    }

                } catch (e: UserNotFoundException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.NotFound, "${e.message}")
                } catch (e: UserBadRequestException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                } catch (e: UserUnauthorizedException){
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                } catch (e: UserInternalErrorException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                } catch (e: IllegalArgumentException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                } catch (e: ConnectException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "EL servidor no esta disponible en este momento.")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor.")
                }
            }

            post("/register") {
                try {
                    val register = call.receive<UserRegisterDTO>()

                    val user = runCatching {
                        userRepository.register(register)
                    }

                    if (user.isSuccess) {
                        call.respond(HttpStatusCode.Created, user.getOrNull()!!)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, user.exceptionOrNull()!!.message!!)
                    }

                } catch (e: UserNotFoundException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.NotFound, "${e.message}")
                } catch (e: UserBadRequestException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                } catch (e: UserInternalErrorException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                }
            }

            get("/storage/{uuid}") {
                try {
                    val uuid = call.parameters["uuid"]
                    val res = runCatching {
                        userRepository.downloadFile(uuid!!)
                    }

                    if (res.isSuccess) {
                        val file = res.getOrNull()!!
                        call.respondFile(file)
                    } else {
                        throw res.exceptionOrNull()!!
                    }

                } catch (e: UserNotFoundException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.NotFound, "${e.message}")
                }  catch (e: UserInternalErrorException) {
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                } catch (e: IllegalArgumentException){
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                } catch (e: Exception){
                    println("Error: ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                }
            }

            authenticate {
                get {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}
                        val res = runCatching {
                            userRepository.findAll("Bearer $token")
                        }

                        if (res.isSuccess) {
                            val users = res.getOrNull()!!
                            call.respond(HttpStatusCode.OK, users)
                        } else {
                            throw res.exceptionOrNull()!!
                        }
                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    }  catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException){
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                get("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val id = call.parameters["id"]

                        try{
                            UUID.fromString(id)
                        } catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@get
                        }

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}
                        val user = runCatching {
                            userRepository.findById("Bearer $token", id!!)
                        }
                        if (user.isSuccess) {
                            call.respond(HttpStatusCode.OK, user.getOrNull()!!)
                        } else {
                            throw user.exceptionOrNull()!!
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException){
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                get("/me") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val subject = originalToken.payload.subject

                        val user = runCatching {
                            userRepository.findMe("Bearer $token", subject)
                        }

                        if (user.isSuccess) {
                            call.respond(HttpStatusCode.OK, user.getOrNull()!!)
                        } else {
                            throw user.exceptionOrNull()!!
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                post("/storage") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val multipart = call.receiveMultipart()
                        var userPhotoDTO: UserPhotoDTO? = null

                        multipart.forEachPart { part ->
                            println(part.contentType)
                            println(part.contentDisposition)
                            println(part.headers)
                            when (part) {
                                is PartData.FileItem  -> {
                                    println("He entrado a fileItem")
                                    val inputStream = part.streamProvider()
                                    val fileBytes = inputStream.readBytes()
                                    val requestBody = fileBytes.toRequestBody("image/png".toMediaTypeOrNull())
                                    val multipartBody = MultipartBody.Part.createFormData("file", "${UUID.randomUUID()}.png", requestBody)
                                    userPhotoDTO = userRepository.uploadFile(token, multipartBody).await()
                                }
                                is PartData.BinaryItem -> {
                                    val inputStream = part.provider()
                                    val fileBytes = inputStream.readBytes()
                                    val requestBody = fileBytes.toRequestBody("image/png".toMediaTypeOrNull())
                                    val multipartBody = MultipartBody.Part.createFormData("file", "${UUID.randomUUID()}.png", requestBody)
                                    userPhotoDTO = userRepository.uploadFile(token, multipartBody).await()
                                }
                                is PartData.BinaryChannelItem -> {
                                    val inputStream = part.provider()
                                    val fileBytes = inputStream.readRemaining().readBytes()
                                    val requestBody = fileBytes.toRequestBody("image/png".toMediaTypeOrNull())
                                    val multipartBody = MultipartBody.Part.createFormData("file", "${UUID.randomUUID()}.png", requestBody)
                                    userPhotoDTO = userRepository.uploadFile(token, multipartBody).await()
                                }
                                is PartData.FormItem -> {
                                    throw BookingMediaNotSupportedException("Este tipo de archivo no está soportado (FileItem)")
                                }
                                else -> {
                                    throw BookingMediaNotSupportedException("Este tipo de archivo no está soportado")
                                }
                            }
                            part.dispose()
                        }
                        call.respond(HttpStatusCode.Created, userPhotoDTO!!)

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }catch (e: Exception){
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                put("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        val id = call.parameters["id"]
                        val userDTO = call.receive<UserUpdateDTO>()

                        try{
                            UUID.fromString(id)
                        }catch(e: IllegalArgumentException) {
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@put
                        }

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}
                        val user = userRepository.findById("Bearer $token", id!!)
                        val updatedUser = runCatching {
                            userRepository.update("Bearer $token", user.uuid, userDTO)
                        }

                        if (updatedUser.isSuccess) {
                            call.respond(HttpStatusCode.OK, updatedUser.getOrNull()!!)
                        } else {
                            throw updatedUser.exceptionOrNull()!!
                        }


                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                put("/me") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val subject = originalToken.subject.toString()
                        val userDTO = call.receive<UserUpdateDTO>()

                        userRepository.findMe("Bearer $token", subject)
                        val updatedUser = runCatching {
                            userRepository.me("Bearer $token", userDTO)
                        }

                        if (updatedUser.isSuccess) {
                            call.respond(HttpStatusCode.OK, updatedUser.getOrNull()!!)
                        } else {
                            throw updatedUser.exceptionOrNull()!!
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    }
                }

                put("/credits/{id}/{creditsAmount}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()
                        val id = call.parameters["id"]

                        try{
                            UUID.fromString(id)
                        } catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@put
                        }

                        val creditsAmount = call.parameters["creditsAmount"]
                        try{
                            creditsAmount!!.toInt()
                        } catch (e: java.lang.NumberFormatException){
                            call.respond(HttpStatusCode.BadRequest, "El número de créditos introducido no es válido: ${e.message}")
                            return@put
                        }

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}
                        val user = userRepository.findById("Bearer $token", id!!)
                        val updatedUser = runCatching {
                            userRepository.updateCredits("Bearer $token", user.uuid, creditsAmount.toInt())
                        }

                        if (updatedUser.isSuccess) {
                            call.respond(HttpStatusCode.OK, updatedUser.getOrNull()!!)
                        } else {
                            throw updatedUser.exceptionOrNull()!!
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                put("/active/{id}/{active}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        val id = call.parameters["id"]

                        try{
                            UUID.fromString(id)
                        } catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@put
                        }

                        val user = userRepository.findMe("Bearer $token", id!!)

                        val active = call.parameters["active"]

                        try{
                            active!!.toBooleanStrict()
                        } catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest, "El estado de actividad introducido del usuario no es válido: ${e.message}")
                            return@put
                        }

                        require(userRole.contains("ADMINISTRATOR")){"Esta operación no está permitida para los usuarios que no son administradores."}

                        val updatedUser = runCatching {
                            userRepository.updateActive("Bearer $token", user.uuid, active.toBooleanStrict())
                        }

                        if (updatedUser.isSuccess) {
                            call.respond(HttpStatusCode.OK, updatedUser.getOrNull()!!)
                        } else {
                            throw updatedUser.exceptionOrNull()!!
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.Unauthorized, "${e.message}")
                    }
                }

                delete("/{id}") {
                    try {
                        val originalToken = call.principal<JWTPrincipal>()!!
                        val token = tokenService.generateToken(originalToken)
                        val userRole = originalToken.payload.getClaim("role").toString()

                        val id = call.parameters["id"]

                        try{
                            UUID.fromString(id)
                        } catch (e: IllegalArgumentException){
                            call.respond(HttpStatusCode.BadRequest, "El id introducido no es válido: ${e.message}")
                            return@delete
                        }

                        if(userRole.contains("ADMINISTRATOR")) {
                            require(bookingsRepository.findByUser(token, id!!).data.isEmpty())
                            { "Se deben actualizar o eliminar las reservas asociadas a este usuario antes de continuar con la operación." }
                            userRepository.findById("Bearer $token", id)

                            userRepository.delete("Bearer $token", id)

                            call.respond(HttpStatusCode.NoContent)
                        }else{
                            call.respond(HttpStatusCode.Unauthorized, "Esta operación no está permitida para los usuarios que no son administradores.")
                        }

                    } catch (e: UserNotFoundException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.NotFound, "${e.message}")
                    } catch (e: UserBadRequestException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    } catch (e: UserInternalErrorException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.InternalServerError, "${e.message}")
                    } catch (e: IllegalArgumentException) {
                        println("Error: ${e.message}")
                        call.respond(HttpStatusCode.BadRequest, "${e.message}")
                    }
                }
            }
        }
    }
}