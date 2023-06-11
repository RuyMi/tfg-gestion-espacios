package es.dam.repositories.space

import es.dam.dto.*
import es.dam.services.space.KtorFitClientSpaces
import es.dam.services.space.RetroFitClientSpaces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.koin.core.annotation.Single
import retrofit2.Call
import java.io.File
import java.nio.file.Files

/**
 * Clase que implementa las operaciones que se pueden realizar sobre espacios. Se comunica con el servicio de espacios mediante Ktorfit y Retrofit.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
class KtorFitSpacesRepository: ISpacesRepository {
    private val client by lazy { KtorFitClientSpaces.instance }
    private val retrofit by lazy { RetroFitClientSpaces.retrofit}

    /**
     * Función que devuelve todos los espacios.
     *
     * @param token Token de autenticación.
     * @return SpaceDataDTO
     */
    override suspend fun findAll(token: String): SpaceDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findAll(token) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting spaces: ${e.message}")
        }
    }

    /**
     * Función que devuelve un espacio por su id.
     *
     * @param token Token de autenticación.
     * @param id Id del espacio.
     * @return SpaceResponseDTO
     */
    override suspend fun findById(token: String, id: String): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.findById(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting space with id $id ${e.message}")
        }
    }

    /**
     * Función que devuelve todos los espacios reservables.
     *
     * @param token Token de autenticación.
     * @param isReservable Booleano que indica si el espacio es reservable o no.
     * @return SpaceDataDTO
     */
    override suspend fun findAllReservables(token: String, isReservable: Boolean): SpaceDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findAllReservables(token, isReservable) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting reservable spaces:${e.message}")
        }
    }

    /**
     * Función que devuelve todos los espacios reservables por su tipo.
     *
     * @param token Token de autenticación.
     * @param isReservable Booleano que indica si el espacio es reservable o no.
     * @return SpaceDataDTO
     */
    override suspend fun findByName(token: String, name: String): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.findByName(token, name) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting space with name $name ${e.message}")
        }
    }

    /**
     * Función que devuelve todos los espacios reservables por su tipo.
     *
     * @param token Token de autenticación.
     * @param isReservable Booleano que indica si el espacio es reservable o no.
     * @return SpaceDataDTO
     */
    override suspend fun create(token: String, entity: SpaceCreateDTO): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.create(token, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error creating space: ${e.message}")
        }
    }

    /**
     * Función que sube un fichero al microservicio.
     *
     * @param token Token de autenticación.
     * @param part Parte del fichero a subir.
     * @return Call<SpacePhotoDTO>
     */
    override suspend fun uploadFile(token: String, part: MultipartBody.Part): Call<SpacePhotoDTO> = withContext(Dispatchers.IO) {
        val call = async { retrofit.uploadFile(part) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error uploading file: ${e.message}")
        }
    }

    /**
     * Función que descarga un fichero del microservicio.
     *
     * @param uuid Uuid del fichero a descargar.
     * @return File
     */
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

    /**
     * Función que actualiza un espacio.
     *
     * @param token Token de autenticación.
     * @param id Id del espacio.
     * @param entity Espacio a actualizar.
     * @return SpaceResponseDTO
     */
    override suspend fun update(token: String, id: String, entity: SpaceUpdateDTO): SpaceResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.update(token, id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating space with id $id: ${e.message}")
        }
    }

    /**
     * Función que elimina un espacio.
     *
     * @param token Token de autenticación.
     * @param id Id del espacio.
     * @return SpaceResponseDTO
     */
    override suspend fun delete(token: String, id: String) = withContext(Dispatchers.IO) {
        val call = async { client.delete(token, id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting space with id $id: ${e.message}")
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


}