package es.dam.services.space

import de.jensklingenberg.ktorfit.Ktorfit
import es.dam.exceptions.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

object KtorFitClientSpaces {
    private const val API_URL = "http://api-spaces:8282/"

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
                        when (response.status) {
                            HttpStatusCode.NotFound -> throw SpaceNotFoundException(response.body<String>())
                            HttpStatusCode.BadRequest -> throw SpaceBadRequestException(response.body<String>())
                            else -> {
                                if (response.status != HttpStatusCode.OK && response.status != HttpStatusCode.Created && response.status != HttpStatusCode.NoContent) {
                                    throw SpaceInternalErrorException(response.body<String>())
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