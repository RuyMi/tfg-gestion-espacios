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

/**
 * Clase que implementa las operaciones que se pueden realizar sobre usuarios. Se comunica con el servicio de usuarios mediante Ktorfit y Retrofit.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
class KtorFitUsersRepository: IUsersRepository {

    private val client by lazy { KtorFitClientUsers.instance }
    private val retrofit by lazy { RetroFitClientUsers.retrofit}

    /**
     * Funcion que te loguea en la aplicacion
     *
     * @param entity UserLoginDTO
     * @return UserTokenDTO
     */
    override suspend fun login(entity: UserLoginDTO): UserTokenDTO = withContext(Dispatchers.IO) {
        val call = async { client.login(entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error logging user: ${e.message}")
        }
    }

    /**
     * Funcion que te registra en la aplicacion
     *
     * @param entity UserRegisterDTO
     * @return UserTokenDTO
     */
    override suspend fun register(entity: UserRegisterDTO): UserTokenDTO = withContext(Dispatchers.IO) {
        val call = async { client.register(entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error registering user: ${e.message}")
        }
    }

    /**
     * Funcion que te descarga una imagen del microservicio
     *
     * @param token Token de autenticacion
     * @param entity UUID de la imagen
     * @return UserResponseDTO
     */
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

    /**
     * Funcion que te sube una imagen al microservicio
     *
     * @param token Token de autenticacion
     * @param file Imagen a subir
     * @return UserPhotoDTO
     */
    override suspend fun uploadFile(token: String, file: MultipartBody.Part): Call<UserPhotoDTO> = withContext(Dispatchers.IO) {
        val call = async { retrofit.uploadFile(file) }
        try {
            println("Dentro del repositorio")
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error uploading file: ${e.message}")
        }
    }

    override suspend fun deleteFile(token: String, uuid: String) = withContext(Dispatchers.IO) {
        val call = async { client.deleteFile(token, uuid) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting file with uuid $uuid: ${e.message}")
        }
    }

    /**
     * Funcion que te devuelve todos los usuarios
     *
     * @param token Token de autenticacion
     * @return UserDataDTO
     */
    override suspend fun findAll(token: String): UserDataDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findAll(token) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting users: ${e.message}")
        }
    }

    /**
     * Funcion que te devuelve un usuario por su id
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @return UserResponseDTO
     */
    override suspend fun findById(token: String, id: String): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findById(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user with id $id ${e.message}")
        }
    }

    /**
     * Funcion que te devuelve el usuario que esta logueado
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @return UserResponseDTO
     */
    override suspend fun findMe(token: String, id: String): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.findMe(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user data ${e.message}")
        }
    }

    /**
     * Funcion que te devuelve si un usuario está activo
     *
     * @param token Token de autenticacion
     * @param username Username del usuario
     * @return UserResponseDTO
     */
    override suspend fun isActive(username: String): Boolean = withContext(Dispatchers.IO)  {
        val call = async { client.isActive(username) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting user's state with username $username ${e.message}")
        }
    }

    /**
     * Funcion que te actualiza un usuario
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @param entity UserUpdateDTO
     * @return UserResponseDTO
     */
    override suspend fun update(token: String, id: String, entity: UserUpdateDTO): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.update(token, id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    /**
     * Funcion que te actualiza un usuario
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @param creditsAmount Cantidad de creditos
     * @return UserResponseDTO
     */
    override suspend fun updateCredits(token: String, id: String, creditsAmount: Int): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateCredits(token, id, creditsAmount) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    /**
     * Funcion que te actualiza un usuario
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @param creditsAmount Cantidad de creditos
     * @return UserResponseDTO
     */
    override suspend fun updateCreditsMe(token: String, id: String, creditsAmount: Int): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateCreditsMe(token, id, creditsAmount) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating me: ${e.message}")
        }
    }

    /**
     * Funcion que te actualiza un usuario el estado de activo
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @param active Estado de activo
     * @return UserResponseDTO
     */
    override suspend fun updateActive(token: String, id: String, active: Boolean): UserResponseDTO = withContext(Dispatchers.IO)  {
        val call = async { client.updateActive(token, id, active) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user with id $id: ${e.message}")
        }
    }

    /**
     * Funcion que te elimina un usuario
     *
     * @param token Token de autenticacion
     * @param id Id del usuario
     * @return UserResponseDTO
     */
    override suspend fun delete(token: String, id: String) = withContext(Dispatchers.IO)  {
        val call = async { client.delete(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting user with id $id: ${e.message}")
        }
    }

    /**
     * Funcion que te actualiza el usuario que esta logueado
     *
     * @param token Token de autenticacion
     * @param entity UserUpdateDTO
     * @return UserResponseDTO
     */
    override suspend fun me(token: String, entity: UserUpdateDTO): UserResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.me(token, entity) }
        try{
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating user: ${e.message}")
        }
    }


}