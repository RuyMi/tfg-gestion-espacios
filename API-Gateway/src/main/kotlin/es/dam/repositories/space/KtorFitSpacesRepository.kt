package es.dam.repositories.space

import de.jensklingenberg.ktorfit.ktorfit
import es.dam.dto.*
import es.dam.services.space.KtorFitClientSpaces
import es.dam.services.space.RetroFitClientSpaces
import io.ktor.client.request.forms.*
import io.ktor.http.content.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.koin.core.annotation.Single
import retrofit2.Call
import java.io.File
import java.nio.file.Files

@Single
class KtorFitSpacesRepository: ISpacesRepository {
    private val client by lazy { KtorFitClientSpaces.instance }
    private val retrofit by lazy { RetroFitClientSpaces.retrofit}

    override suspend fun findAll(token: String): SpaceDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findAll(token) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting spaces: ${e.message}")
        }
    }

    override suspend fun findById(token: String, id: String): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.findById(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting space with id $id ${e.message}")
        }
    }

    override suspend fun findAllReservables(token: String, isReservable: Boolean): SpaceDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findAllReservables(token, isReservable) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting reservable spaces:${e.message}")
        }
    }

    override suspend fun findByName(token: String, name: String): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.findByName(token, name) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting space with name $name ${e.message}")
        }
    }

    override suspend fun create(token: String, entity: SpaceCreateDTO): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.create(token, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error creating space: ${e.message}")
        }
    }

    override suspend fun uploadFile(token: String, part: MultipartBody.Part): Call<SpacePhotoDTO> = withContext(Dispatchers.IO) {
        val call = async { retrofit.uploadFile(part) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error uploading file: ${e.message}")
        }
    }

    override suspend fun downloadFile(uuid: String): File = withContext(Dispatchers.IO) {
        val call = async { client.downloadFile(uuid) }
        try {
            val response =call.await()
            val tempFile = Files.createTempFile("temp", ".png").toFile()
            tempFile.writeBytes(response)
            return@withContext tempFile

        } catch (e: Exception) {
            throw Exception("Error downloading file: ${e.message}")
        }
    }

    override suspend fun update(token: String, id: String, entity: SpaceUpdateDTO): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.update(token, id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating space with id $id: ${e.message}")
        }
    }

    override suspend fun delete(token: String, id: String) = withContext(Dispatchers.IO) {
        val call = async { client.delete(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting space with id $id: ${e.message}")
        }
    }
}