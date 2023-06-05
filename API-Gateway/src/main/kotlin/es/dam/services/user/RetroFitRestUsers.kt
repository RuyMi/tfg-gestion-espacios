package es.dam.services.user

import es.dam.dto.UserPhotoDTO
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetroFitRestUsers {
    @Multipart
    @POST("users/storage")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<UserPhotoDTO>



}