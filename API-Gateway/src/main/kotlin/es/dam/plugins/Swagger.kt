package es.dam.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.AuthScheme
import io.github.smiley4.ktorswaggerui.dsl.AuthType
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

/**
 * Función que configura el swagger. Permite que se pueda acceder a la documentación de la API.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
fun Application.configureSwagger() {
    install(SwaggerUI) {

        routing {
            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        }

        swagger {
            swaggerUrl = "swagger"
            forwardRoot = false
        }

        info {
            title = "LUISVIVES-RESERVAS API REST"
            version = "latest"
            description = "Módulo de proyecto de algunos alumnos de 2ºDAM."
            contact {
                name = "LUISVIVES-RESERVAS"
                url = "https://github.com/RuyMi/tfg-gestion-espacios"
            }
        }

        server {
            url = environment.config.property("server.baseSecureUrl").getString()
            description = "API Gateway que hace de nexo entre los microservicios."
        }

    }
}