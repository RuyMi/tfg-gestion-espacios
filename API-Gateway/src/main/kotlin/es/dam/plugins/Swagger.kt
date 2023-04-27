package es.dam.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.AuthScheme
import io.github.smiley4.ktorswaggerui.dsl.AuthType
import io.ktor.server.application.*

fun Application.configureSwagger() {
    install(SwaggerUI) {

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

        schemasInComponentSection = true
        examplesInComponentSection = true
        automaticTagGenerator = { url -> url.firstOrNull() }
        pathFilter = { method, url ->
            url.contains("test")
        }

        securityScheme("JWT-Auth") {
            type = AuthType.HTTP
            scheme = AuthScheme.BEARER
            bearerFormat = "jwt"
        }
    }
}