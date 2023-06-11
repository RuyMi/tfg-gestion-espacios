package es.dam.config

import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single

/**
 * Clase de configuracion del Storage
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
data class StorageConfig (
    @InjectedParam private val config: Map<String, String>
) {
    val baseUrl = config["baseUrl"].toString()
    val endpoint = config["endpoint"].toString()
}