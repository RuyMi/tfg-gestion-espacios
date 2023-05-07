package es.dam.services.user

import de.jensklingenberg.ktorfit.http.*
import es.dam.dto.*

interface KtorFitRestUsers {
    @GET("users")
    suspend fun findAll(
        @Header("Authorization") token: String
    ): UserDataDTO

    @GET("users/{id}")
    suspend fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): UserResponseDTO

    @POST("users/login")
    suspend fun login(
        @Body user: UserLoginDTO
    ): UserTokenDTO

    @POST("users/register")
    suspend fun register(
        @Body user: UserRegisterDTO
    ): UserTokenDTO

    @PUT("users/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: String, @Body user: UserUpdateDTO
    ): UserResponseDTO

    @DELETE("users/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}