package es.dam.services.space

import es.dam.dto.SpacePhotoDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.UUID

interface RetroFitRestSpaces {
    @Multipart
    @POST("spaces/storage")
    fun uploadFile(@Part file: MultipartBody.Part): Call<SpacePhotoDTO>


    @GET("spaces/storage/{uuid}")
    fun downloadFile(@Path("uuid") uuid: String): File
}