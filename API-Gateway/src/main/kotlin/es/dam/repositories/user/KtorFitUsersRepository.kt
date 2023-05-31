package es.dam.repositories.user

import es.dam.dto.*
import es.dam.services.user.KtorFitClientUsers
import es.dam.services.user.RetroFitClientUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.koin.core.annotation.Single
import retrofit2.Call
import java.io.File
import java.nio.file.Files

//TODO Quitar mensajes excepciones kotlin
@Single
class KtorFitUsersRepository: IUsersRepository {

    private val client by lazy { KtorFitClientUsers.instance }
    private val retrofit by lazy { RetroFitClientUsers.retrofit}

    override suspend fun login(entity: UserLoginDTO): UserTokenDTO = withContext(Dispatchers.IO) {
        val call = async { client.login(entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error logging user: ${e.message}")
        }
    }

    override suspend fun register(entity: UserRegisterDTO): UserTokenDTO = withContext(Dispatchers.IO) {
        val call = async { client.register(entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error registering user: ${e.message}")
        }
    }

    override suspend fun downloadFile(uuid: String): File = withContext(Dispatchers.IO){
        val call = runCatching { client.downloadFile(uuid) }
        try {
            val response = call.getOrThrow()
            val tempFile = Files.createTempFile("temp", ".png").toFile()
            tempFile.writeBytes(response)
            return@withContext tempFile
        } catch (e: Exception) {
            throw Exception("Error downloading file with uuid $uuid: ${e.message}")
        }
    }

    override suspend fun uploadFile(token: String, file: MultipartBody.Part): Call<UserPhotoDTO> {
        val call = runCatching { retrofit.uploadFile(file, token) }
        try {
            return call.getOrThrow()
        } catch (e: Exception) {
            throw Exception("Error uploading file: ${e.message}")
        }
    }

    override suspend fun findAll(token: String): UserDataDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findAll(token) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting users: ${e.message}")
        }
    }

    override suspend fun findById(token: String, id: String): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findById(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user with id $id ${e.message}")
        }
    }

    override suspend fun findMe(token: String, id: String): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findMe(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user data ${e.message}")
        }
    }

    override suspend fun isActive(username: String): Boolean = withContext(Dispatchers.IO)  {
        val call = async { client.isActive(username) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user's state with username $username ${e.message}")
        }
    }

    override suspend fun update(token: String, id: String, entity: UserUpdateDTO): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.update(token, id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    override suspend fun updateCredits(token: String, id: String, creditsAmount: Int): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateCredits(token, id, creditsAmount) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    override suspend fun updateCreditsMe(token: String, id: String, creditsAmount: Int): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateCreditsMe(token, id, creditsAmount) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating me: ${e.message}")
        }
    }

    override suspend fun updateActive(token: String, id: String, active: Boolean): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateActive(token, id, active) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    override suspend fun delete(token: String, id: String) = withContext(Dispatchers.IO)  {
        val call = async { client.delete(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting user with id $id: ${e.message}")
        }
    }

    override suspend fun me(token: String, entity: UserUpdateDTO): UserResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.me(token, entity) }
        try{
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user: ${e.message}")
        }
    }
}