package es.dam.plugins

import es.dam.config.StorageConfig
import es.dam.services.storage.StorageServiceImpl
import io.ktor.server.application.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Application.configureStorage() {
    val storageConfigParams = mapOf(
        "baseUrl" to environment.config.property("server.baseUrl").getString(),
        "uploadDir" to environment.config.property("storage.uploadDir").getString(),
        "endpoint" to environment.config.property("storage.endpoint").getString()
    )

    val storageConfig: StorageConfig = get { parametersOf(storageConfigParams) }
    val storageService: StorageServiceImpl by inject()
    storageService.initStorageDirectory()
}