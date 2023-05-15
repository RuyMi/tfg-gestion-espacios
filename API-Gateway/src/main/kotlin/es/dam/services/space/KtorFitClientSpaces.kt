package es.dam.services.space

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import es.dam.exceptions.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

object KtorFitClientSpaces {
    private const val API_URL = "http://localhost:8282/"

    private val ktorfit by lazy {
        Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    gson()
                }
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                HttpResponseValidator {
                    validateResponse { response ->
                        val status = response
                        when(status.status) {
                            HttpStatusCode.NotFound -> throw SpaceNotFoundException(status.body<String>())
                            HttpStatusCode.BadRequest -> throw SpaceBadRequestException(status.body<String>())
                            else -> {
                                if (status.status != HttpStatusCode.OK && status.status != HttpStatusCode.Created && status.status != HttpStatusCode.NoContent) {
                                    throw SpaceInternalErrorException(status.body<String>())
                                }
                            }
                        }

                    }
                }
            }
            .baseUrl(API_URL)
            .build()
    }

    val instance by lazy {
        ktorfit.create<KtorFitRestSpaces>()
    }
}