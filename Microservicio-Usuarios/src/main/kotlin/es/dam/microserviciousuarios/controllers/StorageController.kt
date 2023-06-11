package es.dam.microserviciousuarios.controllers

import es.dam.microserviciousuarios.dto.SpacePhotoDTO
import es.dam.microserviciousuarios.exceptions.StorageBadRequestException
import es.dam.microserviciousuarios.exceptions.StorageException
import es.dam.microserviciousuarios.service.storage.StorageService
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.time.LocalDateTime

/**
 * Controlador de la API REST de almacenamiento. Permite subir y descargar imágenes.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

@RestController
@RequestMapping("/users/storage")
class StorageController @Autowired constructor(
    private val storageService: StorageService
) {
    @GetMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun serveFile(
        @PathVariable filename: String?,
        request: HttpServletRequest
    ): ResponseEntity<Resource> = runBlocking {

        val myScope = CoroutineScope(Dispatchers.IO)

        val file: Resource = myScope.async { storageService.loadAsResource(filename.toString()) }.await()
        var contentType: String? = null
        contentType = try {
            request.servletContext.getMimeType(file.file.absolutePath)
        } catch (ex: IOException) {
            throw StorageBadRequestException("No se puede determinar el tipo del fichero. -> ${ex.message}")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return@runBlocking ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body<Resource?>(file)
    }

    @PostMapping(
        value = [""],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadFile(
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<SpacePhotoDTO> = runBlocking {
        return@runBlocking try {
            if (!file.isEmpty) {
                val myScope = CoroutineScope(Dispatchers.IO)
                val fileStored = myScope.async { storageService.storeFile(file) }.await()
                val urlStored = storageService.getUrl(fileStored)
                val response =
                    mapOf("url" to urlStored, "fileName" to fileStored, "created_at" to LocalDateTime.now().toString())
                ResponseEntity.status(HttpStatus.CREATED).body(SpacePhotoDTO(response))
            } else {
                throw StorageBadRequestException("No se puede subir un fichero vacío.")
            }
        } catch (e: StorageException) {
            throw StorageBadRequestException(e.message.toString())
        }
    }

    @DeleteMapping(value = ["{filename:.+}"])
    @ResponseBody
    fun deleteFile(
        @PathVariable filename: String?,
        request: HttpServletRequest
    ): ResponseEntity<Resource> = runBlocking {
        try {
            val myScope = CoroutineScope(Dispatchers.IO)
            myScope.launch { storageService.deleteFile(filename.toString()) }.join()
            return@runBlocking ResponseEntity.ok().build()
        } catch (e: StorageException) {
            throw StorageBadRequestException(e.message.toString())
        }
    }
}