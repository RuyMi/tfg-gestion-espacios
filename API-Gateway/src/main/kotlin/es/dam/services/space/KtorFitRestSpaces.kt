package es.dam.services.space

import de.jensklingenberg.ktorfit.http.*
import es.dam.dto.*

interface KtorFitRestSpaces {
    @GET("spaces")
    suspend fun findAll(
        //@Header("Authorization") token: String
    ): SpaceDataDTO

    @GET("spaces/{id}")
    suspend fun findById(
        //@Header("Authorization") token: String,
        @Path("id") id: Long
    ): SpaceResponseDTO

    @GET("spaces/reservables/{isReservable}")
    suspend fun findAllReservables(
        //@Header("Authorization") token: String,
        @Path("isReservable") isReservable: Boolean
    ): SpaceDataDTO

    @GET("spaces/nombre/{name}")
    suspend fun findByName(
        //@Header("Authorization") token: String,
        @Path("name") name: String
    ): SpaceResponseDTO

    @PUT("spaces")
    suspend fun create(
        //@Header("Authorization") token: String,
        @Path("id") id: Long, @Body space: SpaceUpdateDTO
    ): SpaceResponseDTO

    @PUT("spaces")
    suspend fun update(
        //@Header("Authorization") token: String,
        @Path("id") id: Long, @Body space: SpaceUpdateDTO
    ): SpaceResponseDTO

    @DELETE("spaces")
    suspend fun delete(
        //@Header("Authorization") token: String,
        @Path("id") id: Long
    )
}