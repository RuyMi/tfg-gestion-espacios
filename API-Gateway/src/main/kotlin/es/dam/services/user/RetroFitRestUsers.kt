package es.dam.services.user

import es.dam.dto.UserPhotoDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Interfaz que contiene las funciones que se comunican con el microservicio de usuarios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface RetroFitRestUsers {
    @Multipart
    @POST("users/storage")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<UserPhotoDTO>



}