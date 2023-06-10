package es.dam.microserviciousuarios.service.storage

import es.dam.microserviciousuarios.controllers.StorageController
import es.dam.microserviciousuarios.exceptions.StorageBadRequestException
import es.dam.microserviciousuarios.exceptions.StorageNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.stream.Stream
import javax.imageio.ImageIO

@Service
class StorageService(
    @Value("\${upload.root-location}") path: String,
) : IStorageService {
    private val ruta: Path

    init {
        ruta = Paths.get(path)
        this.initStorageService()
    }

    override fun initStorageService() {
        try {
            if (!Files.exists(ruta))
                Files.createDirectory(ruta)
        } catch (e: IOException) {
            throw StorageBadRequestException("Unable to initialize storage service -> ${e.message}")
        }
    }

    override fun getUrl(filename: String): String {
        return MvcUriComponentsBuilder
            .fromMethodName(StorageController::class.java, "serveFile", filename, null)
            .build().toUriString()
    }

    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(ruta, 1)
                .filter { path -> !path.equals(ruta) }
                .map(ruta::relativize)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error reading storage service -> ${e.message}")
        }
    }

    override fun loadFile(fileName: String): Path {
        return ruta.resolve(fileName)
    }

    override fun loadAsResource(filename: String): Resource {
         try {
             return try {
                 val file = File("./uploads/$filename")
                 UrlResource(file.toURI())
             } catch (e: Exception) {
                 val resourceStream = getResourceAsStream("placeholder.png")
                 val imagePlaceHolder: BufferedImage = ImageIO.read(resourceStream)
                 val outputFile = Files.createTempFile("temp", "").toFile()
                 ImageIO.write(imagePlaceHolder, "", outputFile)
                 UrlResource(outputFile.toURI())
             }
        } catch (e: MalformedURLException) {
            throw StorageNotFoundException("No se puede leer fichero: $filename -> ${e.message}")
        }
    }

    override fun storeFile(file: MultipartFile): String {
        val fileName = StringUtils.cleanPath(file.originalFilename.toString())
        val extension = StringUtils.getFilenameExtension(fileName).toString()
        val saved = UUID.randomUUID().toString() + "." + extension

        try {
            if (file.isEmpty) {
                throw StorageBadRequestException("Error storing: $fileName")
            }
            if (fileName.contains("..")) {
                throw StorageBadRequestException("Security path error storing: $fileName")
            }
            file.inputStream.use { inputStream ->
                Files.copy(
                    inputStream, ruta.resolve(saved),
                    StandardCopyOption.REPLACE_EXISTING
                )
                return saved
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Error storing: $fileName -> ${e.message}")
        }
    }

    override fun deleteFile(fileName: String) {
        try {
            val file = loadFile(fileName)
            Files.deleteIfExists(file)
            if (!Files.exists(file)) {
                throw StorageNotFoundException("File $fileName not found.")
            } else {
                Files.delete(file)
            }
        } catch (e: IOException) {
            throw StorageBadRequestException("Error deleting: $fileName -> ${e.message}")
        }
    }

    fun getResourceAsStream(resourceName: String): InputStream? {
        val classLoader = Thread.currentThread().contextClassLoader
        return classLoader.getResourceAsStream(resourceName)?: null
    }
}