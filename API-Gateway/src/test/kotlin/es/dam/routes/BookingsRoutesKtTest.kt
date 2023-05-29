package es.dam.routes

import es.dam.dto.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime
import java.util.*

private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingsRoutesKtTest{

    val loginDTO = UserLoginDTO(
        username = "tEsTiNg",
        password = "admin1234"
    )

    private var bookingId = ""
    private var userId = ""
    private var spaceId = ""

    @BeforeAll
    fun setup() = testApplication{

        val registerDTO = UserRegisterDTO(
            name = "tEsTiNg",
            username = "tEsTiNg",
            email = "tEsTiNg@email.com",
            password = "admin1234",
            userRole = setOf("ADMINISTRATOR"),
            isActive = true
        )

        val spaceCreateDTO = SpaceCreateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

         client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val userUUID = userTokenDTO.user.uuid

        userId = userUUID

        val createSpace = client.post("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }

        val spaceResponse = json.decodeFromString<SpaceResponseDTO>(createSpace.bodyAsText())

        val spaceUUID = spaceResponse.uuid

        spaceId = spaceUUID

        val bookingCreateDTO = BookingCreateDTO(
            userId = userUUID,
            userName = "tEsTiNg",
            spaceId = spaceUUID,
            spaceName = "tEsTiNg",
            startTime = LocalDateTime.parse("2023-05-30T22:23:23.542295200").toString(),
            endTime = LocalDateTime.parse("2023-05-30T22:23:23.542295200").toString(),
            observations = "tEsTiNg"
        )

        val createBooking = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        val bookingResponse = json.decodeFromString<BookingResponseDTO>(createBooking.bodyAsText())

        bookingId = bookingResponse.uuid
    }

    @AfterAll
    fun tearDown() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        client.delete("/bookings/$bookingId/$userId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/spaces/$spaceId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/users/$userId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
    }

    @Test
    fun getAll() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }



    /*@Test
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

        val response = client.get("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getById404() = testApplication {
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

        assertEquals(HttpStatusCode.NotFound, response.status)
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

        val response = client.get("/bookings/space/${spaceId}") {
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

        val response = client.get("/bookings/user/${userId}") {
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

        val response = client.get("/bookings/status/APPROVED") {
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

        val response = client.get("/bookings/time/${spaceId}/2023-05-20") {
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

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        assertEquals(HttpStatusCode.Created, response.status)
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

        val response = client.put("/bookings/${bookingId}") {
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

        val response = client.delete("/bookings/${bookingId}/${userId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

     */
}