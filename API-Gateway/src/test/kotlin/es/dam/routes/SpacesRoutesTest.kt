package es.dam.routes

import es.dam.dto.*
import io.ktor.client.call.*
import io.ktor.server.testing.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpacesRoutesTest {

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

        /*val spaceUUID = spaceResponse.uuid

        spaceId = spaceUUID*/

        /*val bookingCreateDTO = BookingCreateDTO(
            userId = userUUID,
            userName = "tEsTiNg",
            spaceId = spaceUUID,
            spaceName = "tEsTiNg",
            startTime = LocalDateTime.parse("2023-05-30T22:23:23.542295200").toString(),
            endTime = LocalDateTime.parse("2023-05-30T23:23:23.542295200").toString(),
            observations = "tEsTiNg"
        )

        val createBooking = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        val bookingResponse = json.decodeFromString<BookingResponseDTO>(createBooking.bodyAsText())

        bookingId = bookingResponse.uuid*/
    }

    /*@AfterAll
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
    }*/

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

        val response = client.get("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        val body = response.body<SpaceDataDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertTrue(body.data.isNotEmpty()) },
        )
    }

    @Test
    fun findById() = testApplication {
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

        val response = client.get("/spaces/$spaceId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        val body = response.body<SpaceResponseDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertEquals(spaceId, body.uuid) },
        )
    }

    @Test
    fun findById404() = testApplication {
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

        val response = client.get("/spaces/c5b56f72-da70-46f3-8068-492064de351f") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @Test
    fun findById400() = testApplication {
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

        val response = client.get("/spaces/123") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.BadRequest, response.status) },
        )
    }

    @Test
    fun findById403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/$spaceId")
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun getByReservable() = testApplication {
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


        val response = client.get("/spaces/reservables/true"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        val body = response.body<SpaceDataDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertTrue(body.data.isNotEmpty()) },
        )
    }

    @Test
    fun getByReservable404() = testApplication {
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


        val response = client.get("/spacesreservables/false"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @Test
    fun getByReservable400() = testApplication {
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


        val response = client.get("/spaces/reservables/rue"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.BadRequest, response.status) },
        )
    }

    @Test
    fun getByReservable403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/reservables/true")
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun getByNombre() = testApplication {
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


        val response = client.get("/spaces/nombre/Testing"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        val body = response.body<SpaceDataDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertTrue(body.data.isNotEmpty()) },
        )
    }

    @Test
    fun getByNombre404() = testApplication {
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


        val response = client.get("/spaces/nombre/Testing123"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @Test
    fun getByNombre400() = testApplication {
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


        val response = client.get("/spaces/nombre/123"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.BadRequest, response.status) },
        )
    }

    @Test
    fun getByNombre403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/nombre/Testing")
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun create() = testApplication {
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

        val spaceCreateDTO = SpaceCreateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val response = client.post("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }
        val body = response.body<SpaceResponseDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.Created, response.status) },
            { assertEquals(spaceCreateDTO.name, body.name) },
            { assertEquals(spaceCreateDTO.description, body.description) },
            { assertEquals(spaceCreateDTO.image, body.image) },
            { assertEquals(spaceCreateDTO.price, body.price) },
            { assertEquals(spaceCreateDTO.isReservable, body.isReservable) },
            { assertEquals(spaceCreateDTO.requiresAuthorization, body.requiresAuthorization) },
            { assertEquals(spaceCreateDTO.authorizedRoles, body.authorizedRoles) },
            { assertEquals(spaceCreateDTO.bookingWindow, body.bookingWindow) },
        )
    }

    @Test
    fun create400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val spaceCreateDTO = SpaceDataDTO(
            data = emptyList()
        )

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.post("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.BadRequest, response.status) },
        )
    }

    @Test
    fun create403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val spaceCreateDTO = SpaceCreateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val response = client.post("/spaces") {
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun update() = testApplication {
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

        val spaceUpdateDTO = SpaceUpdateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val response = client.put("/spaces/1") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }
        val body = response.body<SpaceResponseDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertEquals(spaceUpdateDTO.name, body.name) },
            { assertEquals(spaceUpdateDTO.description, body.description) },
            { assertEquals(spaceUpdateDTO.image, body.image) },
            { assertEquals(spaceUpdateDTO.price, body.price) },
            { assertEquals(spaceUpdateDTO.isReservable, body.isReservable) },
            { assertEquals(spaceUpdateDTO.requiresAuthorization, body.requiresAuthorization) },
            { assertEquals(spaceUpdateDTO.authorizedRoles, body.authorizedRoles) },
            { assertEquals(spaceUpdateDTO.bookingWindow, body.bookingWindow) },
        )
    }

    @Test
    fun update400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val spaceUpdateDTO = SpaceUpdateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.put("/spaces/1") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.BadRequest, response.status) },
        )
    }

    @Test
    fun update403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val spaceUpdateDTO = SpaceUpdateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val response = client.put("/spaces/1") {
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun update404() = testApplication {
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

        val spaceUpdateDTO = SpaceUpdateDTO(
            name = "tEsTiNg",
            description = "tEsTiNg",
            image = "",
            price = 1,
            isReservable = true,
            requiresAuthorization = false,
            authorizedRoles = setOf("USER"),
            bookingWindow = "10"
        )

        val response = client.put("/spaces/999") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @Test
    fun delete() = testApplication {
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

        val response = client.delete("/spaces/{$spaceId}") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NoContent, response.status) },
        )
    }

    @Test
    fun delete403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.delete("/spaces/{$spaceId}")
        assertAll(
            { assertEquals(HttpStatusCode.Forbidden, response.status) },
        )
    }

    @Test
    fun delete404() = testApplication {
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

        val response = client.delete("/spaces/999") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @Test
    fun getStorage() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/{$spaceId}/storage")
        val file = response.body<File>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertNotNull( file) },
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun createStorage() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val file = File("src/test/resources/test.jpeg")

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/spaces/storage") {
            body = MultiPartFormDataContent(
                formData {
                    append("file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                }
            )
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.Created, response.status) },
        )
    }
}
