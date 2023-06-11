package es.dam.services.space

import es.dam.dto.SpacePhotoDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Interfaz que contiene las funciones que se comunican con el microservicio de espacios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface RetroFitRestSpaces {
    @Multipart
    @POST("spaces/storage")
    fun uploadFile(@Part file: MultipartBody.Part): Call<SpacePhotoDTO>



}