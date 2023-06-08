package es.dam.routes

import es.dam.dto.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import java.io.File
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
        username = "tEsTiNgR",
        password = "admin1234"
    )

    val spaceCreateDTO = SpaceCreateDTO(
        name = "tEsTiNgR",
        description = "tEsTiNg",
        price = 1,
        isReservable = true,
        requiresAuthorization = false,
        authorizedRoles = setOf("USER"),
        bookingWindow = 10
    )

    private var userId = ""
    private var spaceId = ""
    private var spaceCreateId = ""


    @BeforeAll
    fun setup() = testApplication{

        val registerDTO = UserRegisterDTO(
            name = "tEsTiNgR",
            username = "tEsTiNgR",
            email = "tEsTiNgR@email.com",
            password = "admin1234",
            userRole = setOf("ADMINISTRATOR"),
            isActive = true
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

        client.delete("/spaces/$spaceId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/spaces/$spaceCreateId") {
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
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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


        val response = client.get("/spaces/nombre/tEsTiNg"){
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        val body = response.body<SpaceResponseDTO>()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertNotNull(body) },
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
    fun getByNombre403() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/nombre/Testing")
        assertAll(
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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
            bookingWindow = 10
        )

        val response = client.post("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }
        val body = response.body<SpaceResponseDTO>()

        spaceCreateId = body.uuid

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
            bookingWindow = 10
        )

        val response = client.post("/spaces") {
            contentType(ContentType.Application.Json)
            setBody(spaceCreateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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
            bookingWindow = 10
        )

        val create = client.post("/spaces") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }

        spaceId = create.body<SpaceResponseDTO>().uuid

        val response = client.put("/spaces/$spaceId") {
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
            bookingWindow = 10
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
            bookingWindow = 10
        )

        val response = client.put("/spaces/1") {
            contentType(ContentType.Application.Json)
            setBody(spaceUpdateDTO)
        }
        assertAll(
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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
            bookingWindow = 10
        )

        val response = client.put("/spaces/ab3e47c3-016a-49b7-8616-262da0c86553") {
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

        val response = client.delete("/spaces/${spaceId}") {
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
            { assertEquals(HttpStatusCode.Unauthorized, response.status) },
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

        val response = client.delete("/spaces/106ae131-94b5-458f-a1ad-8b347361b281") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status) },
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getStorage() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/spaces/storage/$spaceId")
        val file = response.content.toByteArray()
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status) },
            { assertEquals("image/png", response.headers[HttpHeaders.ContentType]) },
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
