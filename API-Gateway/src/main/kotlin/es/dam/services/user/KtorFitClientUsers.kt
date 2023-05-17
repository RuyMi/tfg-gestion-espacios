package es.dam.services.user

import de.jensklingenberg.ktorfit.Ktorfit
import es.dam.exceptions.UserBadRequestException
import es.dam.exceptions.UserInternalErrorException
import es.dam.exceptions.UserNotFoundException
import es.dam.exceptions.UserUnauthorizedException
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


object KtorFitClientUsers {
    private const val API_URL = "http://localhost:8383/"

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
                            HttpStatusCode.NotFound -> throw UserNotFoundException(status.body<String>())
                            HttpStatusCode.BadRequest -> throw UserBadRequestException(status.body<String>())
                            HttpStatusCode.Unauthorized -> throw UserUnauthorizedException(status.body<String>())
                            else -> {
                                if (status.status != HttpStatusCode.OK && status.status != HttpStatusCode.Created && status.status != HttpStatusCode.NoContent) {
                                    throw UserInternalErrorException(status.body<String>())
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
        ktorfit.create<KtorFitRestUsers>()
    }
}