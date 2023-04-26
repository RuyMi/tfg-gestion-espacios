package es.dam.plugins

import es.dam.db.MongoDbManager
import es.dam.repositories.SpaceRepositoryImpl
import es.dam.services.SpaceServiceImpl
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            module {
                single { SpaceRepositoryImpl() }
                single(named("SpaceServiceImpl")) { SpaceServiceImpl(get())}
            }
        )
    }
}