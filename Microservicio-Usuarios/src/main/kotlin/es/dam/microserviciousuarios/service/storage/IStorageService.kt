package es.dam.microserviciousuarios.service.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface IStorageService {
    fun initStorageService()
    fun getUrl(filename: String): String
    fun loadAll(): Stream<Path>
    fun loadFile(fileName: String): Path
    fun loadAsResource(filename: String): Resource
    fun storeFile(file: MultipartFile): String
    fun deleteFile(fileName: String)
}