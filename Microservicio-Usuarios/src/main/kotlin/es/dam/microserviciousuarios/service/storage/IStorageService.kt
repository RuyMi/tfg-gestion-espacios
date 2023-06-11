package es.dam.microserviciousuarios.service.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

/**
 * Interfaz del servicio de almacenamiento. Se encarga de definir los métodos que debe implementar el servicio de almacenamiento.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

interface IStorageService {
    fun initStorageService()
    fun getUrl(filename: String): String
    fun loadAll(): Stream<Path>
    fun loadFile(fileName: String): Path
    fun loadAsResource(filename: String): Resource
    fun storeFile(file: MultipartFile): String
    fun deleteFile(fileName: String)
}