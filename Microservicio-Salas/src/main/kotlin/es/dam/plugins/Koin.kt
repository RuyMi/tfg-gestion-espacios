package es.dam.plugins

import es.dam.config.StorageConfig
import es.dam.repositories.SpaceRepositoryImpl
import es.dam.services.spaces.SpaceServiceImpl
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            module {
                single { SpaceRepositoryImpl() }
                single(named("SpaceServiceImpl")) { SpaceServiceImpl(get()) }
                single { StorageConfig(get()) }
                single(named("StorageServiceImpl")) { SpaceServiceImpl(get()) }
            }
        )
    }
}