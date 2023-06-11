package es.dam.services.booking

import de.jensklingenberg.ktorfit.Ktorfit
import es.dam.exceptions.BookingBadRequestException
import es.dam.exceptions.BookingInternalErrorException
import es.dam.exceptions.BookingNotFoundException
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

/**
 * Objeto que contiene la configuración del cliente Ktorfit para el microservicio de reservas.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
object KtorFitClientBookings {
    private const val API_URL = "http://api-bookings:8181/"

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
                            HttpStatusCode.NotFound -> throw BookingNotFoundException(status.body<String>())
                            HttpStatusCode.BadRequest -> throw BookingBadRequestException(status.body<String>())
                            else -> {
                                if (status.status != HttpStatusCode.OK && status.status != HttpStatusCode.Created && status.status != HttpStatusCode.NoContent) {
                                    throw BookingInternalErrorException(status.body<String>())
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
        ktorfit.create<KtorFitRestBookings>()
    }
}