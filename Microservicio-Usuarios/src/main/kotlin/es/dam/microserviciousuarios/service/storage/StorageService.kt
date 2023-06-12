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

/**
 * Clase que implementa el servicio de almacenamiento. Implementa la interfaz [IStorageService]. Se encarga de gestionar el almacenamiento de imágenes.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Service
class StorageService(
    @Value("\${upload.root-location}") path: String,
) : IStorageService {
    private val ruta: Path

    /**
     * Inicializa el servicio de almacenamiento.
     */
    init {
        ruta = Paths.get(path)
        this.initStorageService()
    }

    /**
     * Inicializa el servicio de almacenamiento.
     *
     */
    override fun initStorageService() {
        try {
            if (!Files.exists(ruta))
                Files.createDirectory(ruta)
        } catch (e: IOException) {
            throw StorageBadRequestException("Unable to initialize storage service -> ${e.message}")
        }
    }

    /**
     * Devuelve la url de la imagen.
     *
     * @param filename nombre de la imagen
     * @return url de la imagen
     */
    override fun getUrl(filename: String): String {
        return MvcUriComponentsBuilder
            .fromMethodName(StorageController::class.java, "serveFile", filename, null)
            .build().toUriString()
    }

    /**
     * Devuelve la ruta de todas las imagenes.
     *
     * @return ruta de todas las imagenes
     */
    override fun loadAll(): Stream<Path> {
        return try {
            Files.walk(ruta, 1)
                .filter { path -> !path.equals(ruta) }
                .map(ruta::relativize)
        } catch (e: IOException) {
            throw StorageBadRequestException("Error reading storage service -> ${e.message}")
        }
    }

    /**
     * Devuelve la ruta de una imagen.
     *
     * @param fileName nombre de la imagen
     * @return ruta de una imagen
     */
    override fun loadFile(fileName: String): Path {
        return ruta.resolve(fileName)
    }

    /**
     * Devuelve la imagen como un recurso.
     *
     * @param filename nombre de la imagen
     * @return imagen como un recurso
     */
    override fun loadAsResource(filename: String): Resource {
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

    }

    /**
     * Guarda una imagen.
     *
     * @param file imagen a guardar
     * @return nombre de la imagen guardada
     */
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

    /**
     * Borra una imagen.
     *
     * @param fileName nombre de la imagen a borrar
     */
    override fun deleteFile(fileName: String) {
        val file = File("./uploads/$fileName")
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * Devuelve un recurso como un stream.
     *
     * @param resourceName nombre del recurso
     * @return recurso como un stream
     */
    fun getResourceAsStream(resourceName: String): InputStream? {
        val classLoader = Thread.currentThread().contextClassLoader
        return classLoader.getResourceAsStream(resourceName)?: null
    }
}