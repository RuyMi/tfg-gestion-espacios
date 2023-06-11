package es.dam.services.storage

import es.dam.config.StorageConfig
import es.dam.exceptions.StorageException
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.time.LocalDateTime
import javax.imageio.ImageIO

/**
 * Clase que implementa el servicio de almacenamiento. Implementa la interfaz [StorageService]. Se encarga de gestionar el almacenamiento de imágenes.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
class StorageServiceImpl(
    @InjectedParam private val storageConfig: StorageConfig
) : StorageService {
    override fun getConfig(): StorageConfig {
        return storageConfig
    }

    private val uploadsDir = File("./uploads")

    /**
     * Inicializa el servicio de almacenamiento.
     *
     */
    override fun initStorageDirectory() {
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs()
        }
    }

    /**
     * Guarda una imagen en el directorio de almacenamiento.
     *
     * @param fileName Nombre del fichero.
     * @param fileBytes Bytes del fichero.
     * @return Mapa con los datos del fichero.
     */
    override suspend fun saveFile(fileName: String, fileBytes: ByteArray): Map<String, String> =
        withContext(Dispatchers.IO) {
            try {
                val file = File("./uploads/$fileName")
                file.writeBytes(fileBytes)
                return@withContext mapOf(
                    "fileName" to fileName,
                    "createdAt" to LocalDateTime.now().toString(),
                    "size" to fileBytes.size.toString(),
                    "baseUrl" to storageConfig.baseUrl + "/" + storageConfig.endpoint + "/" + fileName,
                )
            } catch (e: Exception) {
                throw StorageException.FileNotFound("Error al guardar el fichero: ${e.message}")
            }
        }

    /**
     * Guarda una imagen en el directorio de almacenamiento.
     *
     * @param fileName Nombre del fichero.
     * @param fileBytes Bytes del fichero.
     * @return Mapa con los datos del fichero.
     */
    override suspend fun saveFile(fileName: String, fileBytes: ByteReadChannel): Map<String, String> =
        withContext(Dispatchers.IO) {
            try {
                val file = File("./uploads/$fileName")
                val res = fileBytes.copyAndClose(file.writeChannel())
                return@withContext mapOf(
                    "fileName" to fileName,
                    "createdAt" to LocalDateTime.now().toString(),
                    "size" to res.toString(),
                    "baseUrl" to storageConfig.baseUrl + "/" + storageConfig.endpoint + "/" + fileName,
                )
            } catch (e: Exception) {
                throw StorageException.FileNotSave("Error al guardar el fichero: ${e.message}")
            }
        }

    /**
     * Obtiene una imagen del directorio de almacenamiento.
     *
     * @param fileName Nombre del fichero.
     * @return Fichero.
     */
    override suspend fun getFile(fileName: String): File = withContext(Dispatchers.IO) {
        try {
            val file = File("./uploads/$fileName")
            return@withContext file
        } catch (e: Exception) {
            val resourceStream = getResourceAsStream("placeholder.png")
            val imagePlaceHolder: BufferedImage = ImageIO.read(resourceStream)
            val outputFile = Files.createTempFile("temp", "").toFile()
            ImageIO.write(imagePlaceHolder, "", outputFile)
            return@withContext outputFile
        }
    }

    /**
     * Elimina una imagen del directorio de almacenamiento.
     *
     * @param fileName Nombre del fichero.
     */
    override suspend fun deleteFile(fileName: String): Unit = withContext(Dispatchers.IO) {
        val file = File("./uploads/$fileName")
        if (!file.exists()) {
            throw StorageException.FileNotFound("No se ha encontrado el fichero: $fileName")
        } else {
            file.delete()
        }
    }

    /**
     * Obtiene un recurso como un stream.
     *
     * @param resourceName Nombre del recurso.
     * @return Stream del recurso.
     */
    private fun getResourceAsStream(resourceName: String): InputStream? {
        val classLoader = Thread.currentThread().contextClassLoader
        return classLoader.getResourceAsStream(resourceName)?: null
    }
}