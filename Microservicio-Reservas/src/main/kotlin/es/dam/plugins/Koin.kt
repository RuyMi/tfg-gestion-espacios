package es.dam.plugins

import es.dam.repositories.BookingCachedRepository
import es.dam.repositories.BookingRepositoryImpl
import es.dam.services.BookingServiceImpl
import es.dam.services.cache.BookingCacheImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

/**
 * Configuración de Koin para la inyección de dependencias del microservicio.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Application.configureKoin() {
    install(Koin) {
        modules(
            module {
                single { BookingRepositoryImpl() }
                single { BookingServiceImpl(get())}
                single { BookingCachedRepository(get(),get()) }
                single { BookingCacheImpl() }
            }
        )
    }
}