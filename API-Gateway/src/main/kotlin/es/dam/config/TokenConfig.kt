package es.dam.config

import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single

/**
 * Clase que implementa la configuración del token.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
data class TokenConfig(
    @InjectedParam private val config: Map<String, String>
) {
    val audience = config["audience"].toString()
    val secret = config["secret"].toString()
    val issuer = config["issuer"].toString()
    val realm = config["realm"].toString()
}