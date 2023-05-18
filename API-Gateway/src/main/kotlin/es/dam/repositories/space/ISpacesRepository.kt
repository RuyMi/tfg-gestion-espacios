package es.dam.repositories.space

import es.dam.dto.*
import io.ktor.client.request.forms.*
import io.ktor.http.content.*
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

interface ISpacesRepository {
    suspend fun findAll(token: String): SpaceDataDTO
    suspend fun findById(token: String, id: String): SpaceResponseDTO
    suspend fun findAllReservables(token: String, isReservable: Boolean): SpaceDataDTO
    suspend fun findByName(token: String, name: String): SpaceResponseDTO
    suspend fun create(token: String,  entity: SpaceCreateDTO): SpaceResponseDTO
    suspend fun uploadFile(token: String, part: MultipartBody.Part): Call<SpacePhotoDTO>
    suspend fun downloadFile(uuid: String): File
    suspend fun update(token: String, id: String, entity: SpaceUpdateDTO): SpaceResponseDTO
    suspend fun delete(token: String, id: String)
}