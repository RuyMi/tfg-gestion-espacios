package es.dam.routes

import es.dam.dto.*
import es.dam.repositories.booking.KtorFitBookingsRepository
import es.dam.repositories.user.KtorFitUsersRepository
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@OptIn(ExperimentalCoroutinesApi::class)
class BookingsRoutesKtTest{
    val jsonPerso = Json { ignoreUnknownKeys = true }

    private val registerDTO = UserRegisterDTO(
        name = "testi",
        username = "testi",
        email = "wanai@email.com",
        password = "admin1234",
        userRole = setOf("ADMINISTRATOR")
    )

    private val loginDTO = UserLoginDTO(
        username = "testi",
        password = "admin1234"
    )

    private val bookingCreateDTO = BookingCreateDTO(
        userId = UUID.fromString("5cf4eb15-a5da-4a49-b80c-90b408fd0c03").toString(),
        spaceId = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166d").toString(),
        startTime = LocalDateTime.parse("2023-05-20T22:23:23.542295200").toString(),
        endTime = LocalDateTime.parse("2023-05-20T22:23:23.542295200").toString()
    )

    private val bookingUpdateDTO = BookingUpdateDTO(
            userId = UUID.fromString("5cf4eb15-a5da-4a49-b80c-90b408fd0c03").toString(),
            spaceId = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166d").toString(),
            startTime = LocalDateTime.parse("2023-05-20T22:23:23.542295200").toString(),
            endTime = LocalDateTime.parse("2023-05-20T22:23:23.542295200").toString(),
            status = "PENDING"
    )

    @Test
    fun getAll() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getById() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/ebc0910c-8c10-41c1-b0cd-9a5e44a90506") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getBySpaceId() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/space/c060c959-8462-4a0f-9265-9af4f54d166d") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByUserId() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/user/c060c959-8462-4a0f-9265-9af4f54d166c") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByUserIduser() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())
        println(dto.token)

        val response = client.get("/users/17892052-d935-422b-b886-1fcc76aa49df") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }


        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByStatus() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/status/PENDING") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByTime() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/time/c060c959-8462-4a0f-9265-9af4f54d166d/2023-05-20") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun post() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())
        println("-----------------------------" + dto.token)
        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun put() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.put("/bookings/ebc0910c-8c10-41c1-b0cd-9a5e44a90506") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun delete() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        /*val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }*/

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/ebc0910c-8c10-41c1-b0cd-9a5e44a90506/c060c959-8462-4a0f-9265-9af4f54d166c") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
