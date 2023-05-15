package es.dam.services.space

import de.jensklingenberg.ktorfit.http.*
import es.dam.dto.*
import io.ktor.client.request.forms.*
import io.ktor.http.content.*

interface KtorFitRestSpaces {
    @GET("spaces")
    suspend fun findAll(
        @Header("Authorization") token: String
    ): SpaceDataDTO

    @GET("spaces/{id}")
    suspend fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SpaceResponseDTO

    @GET("spaces/reservables/{isReservable}")
    suspend fun findAllReservables(
        @Header("Authorization") token: String,
        @Path("isReservable") isReservable: Boolean
    ): SpaceDataDTO

    @GET("spaces/nombre/{name}")
    suspend fun findByName(
        @Header("Authorization") token: String,
        @Path("name") name: String
    ): SpaceResponseDTO

    @POST("spaces")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body space: SpaceCreateDTO
    ): SpaceResponseDTO

    @Multipart
    @POST("spaces/storage")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Part("image") image: MultiPartFormDataContent
    ): SpacePhotoDTO


    @PUT("spaces/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: String, @Body space: SpaceUpdateDTO
    ): SpaceResponseDTO

    @DELETE("spaces/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}