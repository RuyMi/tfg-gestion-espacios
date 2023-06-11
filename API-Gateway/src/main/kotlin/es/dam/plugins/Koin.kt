package es.dam.plugins

import io.ktor.server.application.*
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin

/**
 * Función que configura Koin. Permite que se puedan inyectar las dependencias.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Application.configureKoin() {
    install(Koin) {
        defaultModule()
    }
}