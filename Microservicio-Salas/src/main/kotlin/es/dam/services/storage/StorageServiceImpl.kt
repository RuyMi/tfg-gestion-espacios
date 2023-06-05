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

@Single
class StorageServiceImpl(
    @InjectedParam private val storageConfig: StorageConfig
) : StorageService {
    override fun getConfig(): StorageConfig {
        return storageConfig
    }
    private val resourcePath = this::class.java.classLoader.getResource("uploads").file
    private val uploadsPath = resourcePath
    private val uploadsDir = File(uploadsPath)

    override fun initStorageDirectory() {
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs()
        }
    }

    override suspend fun saveFile(fileName: String, fileBytes: ByteArray): Map<String, String> =
        withContext(Dispatchers.IO) {
            try {
                val file = File("${uploadsPath}/$fileName")
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

    override suspend fun saveFile(fileName: String, fileBytes: ByteReadChannel): Map<String, String> =
        withContext(Dispatchers.IO) {
            try {
                val file = File("${uploadsPath}/$fileName")
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

    override suspend fun getFile(fileName: String): File = withContext(Dispatchers.IO) {
        var resourceStream = getResourceAsStream("uploads/$fileName")
         if (resourceStream == null) {
            resourceStream = getResourceAsStream("placeholder.jpeg")
            val imagePlaceHolder: BufferedImage = ImageIO.read(resourceStream)
            val outputFile = Files.createTempFile("temp", "").toFile()
            ImageIO.write(imagePlaceHolder, "", outputFile)
            return@withContext outputFile
        } else {
            val imagePlaceHolder: BufferedImage = ImageIO.read(resourceStream)
            val outputFile = Files.createTempFile("temp", "").toFile()
            ImageIO.write(imagePlaceHolder, "", outputFile)
            return@withContext outputFile
        }
    }

    override suspend fun deleteFile(fileName: String): Unit = withContext(Dispatchers.IO) {
        val file = File("${uploadsPath}/$fileName")
        if (!file.exists()) {
            throw StorageException.FileNotFound("No se ha encontrado el fichero: $fileName")
        } else {
            file.delete()
        }
    }

    fun getResourceAsStream(resourceName: String): InputStream? {
        val classLoader = Thread.currentThread().contextClassLoader
        return classLoader.getResourceAsStream(resourceName)?: null
    }
}