package es.dam.services.storage

import es.dam.config.StorageConfig
import io.ktor.utils.io.*
import java.io.File

/**
 * Interfaz que define las operaciones del servicio de almacenamiento.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface StorageService {
    fun getConfig(): StorageConfig
    fun initStorageDirectory()
    suspend fun saveFile(fileName: String, fileBytes: ByteArray): Map<String, String>
    suspend fun saveFile(fileName: String, fileBytes: ByteReadChannel): Map<String, String>
    suspend fun getFile(fileName: String): File
    suspend fun deleteFile(fileName: String)
}