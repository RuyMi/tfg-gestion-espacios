package es.dam.services.booking

import de.jensklingenberg.ktorfit.http.*
import es.dam.dto.*

interface KtorFitRestBookings {
    @GET("bookings")
    suspend fun findAll(
        @Header("Authorization") token: String
    ): BookingDataDTO

    @GET("bookings/{id}")
    suspend fun findById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): BookingResponseDTO

    @GET("bookings/space/{id}")
    suspend fun findBySpace(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): BookingDataDTO

    @GET("bookings/user/{id}")
    suspend fun findByUser(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): BookingDataDTO

    @GET("bookings/status/{status}")
    suspend fun findByStatus(
        @Header("Authorization") token: String,
        @Path("status") status: String
    ): BookingDataDTO

    @GET("bookings/time/{id}/{date}")
    suspend fun findByTime(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Path("date") date: String
    ): BookingDataDTO

    @POST("bookings")
    suspend fun create(
        @Header("Authorization") token: String,
        @Body booking: BookingCreateDTO
    ): BookingResponseDTO

    @PUT("bookings/{id}")
    suspend fun update(
        @Header("Authorization") token: String,
        @Path("id") id: String, @Body booking: BookingUpdateDTO
    ): BookingResponseDTO

    @DELETE("bookings/{id}")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}