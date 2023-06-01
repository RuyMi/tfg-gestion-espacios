package es.dam.services.user

import de.jensklingenberg.ktorfit.http.Header
import es.dam.dto.SpacePhotoDTO
import es.dam.dto.UserPhotoDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.UUID

interface RetroFitRestUsers {
    @Multipart
    @POST("users/storage")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<UserPhotoDTO>



}