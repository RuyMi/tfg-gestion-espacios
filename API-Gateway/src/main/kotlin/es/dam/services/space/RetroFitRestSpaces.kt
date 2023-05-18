package es.dam.services.space

import es.dam.dto.SpacePhotoDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.UUID

interface RetroFitRestSpaces {
    @Multipart
    @POST("spaces/storage")
    fun uploadFile(@Part file: MultipartBody.Part): Call<SpacePhotoDTO>



}