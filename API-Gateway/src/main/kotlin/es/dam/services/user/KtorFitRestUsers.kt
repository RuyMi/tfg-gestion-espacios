package es.dam.services.user

import de.jensklingenberg.ktorfit.http.*
import es.dam.dto.*
import io.ktor.client.request.forms.*

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

    @GET("users/me/{id}")
    suspend fun findMe(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): UserResponseDTO

    @GET("users/isActive/{username}")
    suspend fun isActive(
            @Path("username") username: String
    ): Boolean

    @GET("users/storage/{uuid}")
    suspend fun downloadFile(
        @Path("uuid") uuid: String
    ): ByteArray

    @POST("users/login")
    suspend fun login(
        @Body user: UserLoginDTO
    ): UserTokenDTO

    @PUT("users/credits/{id}/{creditsAmount}")
    suspend fun updateCredits(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Path("creditsAmount") creditsAmount: Int
    ): UserResponseDTO

    @PUT("users/credits/me/{id}/{creditsAmount}")
    suspend fun updateCreditsMe(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Path("creditsAmount") creditsAmount: Int
    ): UserResponseDTO

    @PUT("users/active/{id}/{active}")
    suspend fun updateActive(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Path("active") active: Boolean
    ): UserResponseDTO

    @POST("users/register")
    suspend fun register(
        @Body user: UserRegisterDTO
    ): UserTokenDTO

    @PUT("users/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: String, @Body user: UserUpdateDTO
    ): UserResponseDTO

    @POST("users/storage")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Body image: MultiPartFormDataContent
    ): UserPhotoDTO

    @PUT("users/me")
    suspend fun me(
        @Header("Authorization") token: String,
        @Body user: UserUpdateDTO
    ): UserResponseDTO

    @DELETE("users/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}