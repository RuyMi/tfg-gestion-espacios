package es.dam.plugins

import es.dam.config.TokenConfig
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.space.KtorFitSpacesRepository
import es.dam.repositories.user.KtorFitUsersRepository
import es.dam.services.token.TokensService
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
//import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {

        defaultModule()
    }
}