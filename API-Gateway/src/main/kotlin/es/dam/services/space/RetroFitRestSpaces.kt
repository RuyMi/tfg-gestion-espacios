package es.dam.services.space

import es.dam.dto.SpacePhotoDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetroFitRestSpaces {
    @Multipart
    @POST("spaces/storage")
    fun uploadFile(@Part file: MultipartBody.Part): Call<SpacePhotoDTO>



}