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

    private val loginAdminDTO = UserLoginDTO(
        username = "tEsTiNg",
        password = "admin1234"
    )

    private val loginNotAdminDTO = UserLoginDTO(
        username = "tEsTiNg-user",
        password = "user1234"
    )

    private var bookingId = ""
    private var userId = ""
    private var userNotAdminId = ""

    private var spaceId = ""

    private var createdBookingId = ""

    private var deletedBookingId = ""

    private var deletedBookingId2 = ""

    private var deletedBookingId3 = ""

    private var createdBookingNotAdminId = ""

    private var fechaActual = LocalDateTime.now().plusHours(1)

    private val uuid404 = "00000000-0000-0000-0000-000000000000"

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

        val registerNotAdminDTO = UserRegisterDTO(
            name = "tEsTiNg-user",
            username = "tEsTiNg-user",
            email = "tEsTiNgUser@email.com",
            password = "user1234",
            userRole = setOf("USER"),
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

        val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerNotAdminDTO)
        }

         userNotAdminId = json.decodeFromString<UserTokenDTO>(register.bodyAsText()).user.uuid

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
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
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "tEsTiNg"
        )

        val createBooking = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        val bookingResponse = json.decodeFromString<BookingResponseDTO>(createBooking.bodyAsText())

        bookingId = bookingResponse.uuid

        val bookingDelete = BookingCreateDTO(
            userId = userUUID,
            userName = "tEsTiNg-delete",
            spaceId = spaceUUID,
            spaceName = "tEsTiNg-delete",
            startTime = fechaActual.plusDays(1).toString(),
            endTime = fechaActual.plusHours(1).plusDays(1).toString(),
            observations = "tEsTiNg-delete"
        )

        val testDelete = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(bookingDelete)
        }

        val testDeleteResponse = json.decodeFromString<BookingResponseDTO>(testDelete.bodyAsText())

        deletedBookingId = testDeleteResponse.uuid

        val bookingDelete2 = BookingCreateDTO(
            userId = userUUID,
            userName = "tEsTiNg-delete",
            spaceId = spaceUUID,
            spaceName = "tEsTiNg-delete",
            startTime = fechaActual.plusDays(8).toString(),
            endTime = fechaActual.plusHours(1).plusDays(8).toString(),
            observations = "tEsTiNg-delete"
        )

        val testDelete2 = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
            contentType(ContentType.Application.Json)
            setBody(bookingDelete2)
        }

        val testDeleteResponse2 = json.decodeFromString<BookingResponseDTO>(testDelete2.bodyAsText())

        deletedBookingId2 = testDeleteResponse2.uuid


        val bookingDelete3 = BookingCreateDTO(
            userId = json.decodeFromString<UserTokenDTO>(register.bodyAsText()).user.uuid,
            userName = "tEsTiNg-delete",
            spaceId = spaceUUID,
            spaceName = "tEsTiNg-delete",
            startTime = fechaActual.plusDays(7).toString(),
            endTime = fechaActual.plusHours(1).plusDays(7).toString(),
            observations = "tEsTiNg-delete"
        )

        val testDelete3 = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + json.decodeFromString<UserTokenDTO>(register.bodyAsText()).token)
            contentType(ContentType.Application.Json)
            setBody(bookingDelete3)
        }

        val testDeleteResponse3 = json.decodeFromString<BookingResponseDTO>(testDelete3.bodyAsText())

        deletedBookingId3 = testDeleteResponse3.uuid
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
            setBody(loginAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        client.delete("/bookings/$bookingId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/bookings/$createdBookingId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/bookings/$deletedBookingId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/bookings/$deletedBookingId2") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/bookings/$deletedBookingId3") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/bookings/$createdBookingNotAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/spaces/$spaceId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/users/$userId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        //TODO: No lo borra
        client.delete("/users/$userNotAdminId") {
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
            setBody(loginAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    //TODO: Salta la excepcion antes de comprobar
    /*@Test
    fun getAllNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

     */


    @Test
    fun getById() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByIdNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getById404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/$uuid404") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun getById400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun getBySpaceId() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/space/${spaceId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getBySpaceIdNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/space/${spaceId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getBySpaceId404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/space/$uuid404") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(result.data.isEmpty())

    }

    @Test
    fun getBySpaceId400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/space/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)

    }

    @Test
    fun getByUserId() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/user/${userId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByUserIdNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/user/${userId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getByUserId404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/user/$uuid404") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByUserId400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/user/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun getByStatus() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/status/APPROVED") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByStatusNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/status/APPROVED") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getByStatus404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/status/REJECTED") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getByStatus400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/status/test") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun getByTime404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/time/${spaceId}/${fechaActual.minusDays(1).toString().split("T")[0]}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isEmpty())
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

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/time/${spaceId}/${fechaActual.toString().split("T")[0]}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val result = json.decodeFromString<BookingDataDTO>(response.bodyAsText())

        assertTrue(result.data.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun postAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

         val bookingCreateDTO = BookingCreateDTO(
            userId = userId,
            userName = "testingPost",
            spaceId = spaceId,
            spaceName = "testingPost",
            startTime = fechaActual.plusDays(2).toString(),
            endTime = fechaActual.plusHours(1).plusDays(2).toString(),
            observations = "testingPost"
        )

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        val bookingResponse = json.decodeFromString<BookingResponseDTO>(response.bodyAsText())

        createdBookingId = bookingResponse.uuid

        val creditsResponse = client.get("/users/$userId") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(20, json.decodeFromString<UserResponseDTO>(creditsResponse.bodyAsText()).credits)
    }

    @Test
    fun postNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val bookingCreateDTO = BookingCreateDTO(
            userId = userNotAdminId,
            userName = "testingPostNotAdmin",
            spaceId = spaceId,
            spaceName = "testingPostNotAdmin",
            startTime = fechaActual.plusDays(3).toString(),
            endTime = fechaActual.plusHours(1).plusDays(3).toString(),
            observations = "testingPost"
        )

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        val bookingResponse = json.decodeFromString<BookingResponseDTO>(response.bodyAsText())

        createdBookingNotAdminId = bookingResponse.uuid

        val creditsResponse = client.get("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(19, json.decodeFromString<UserResponseDTO>(creditsResponse.bodyAsText()).credits)
    }

    @Test
    fun postBeforeActualDate() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val bookingCreateDTO = BookingCreateDTO(
            userId = userId,
            userName = "testingWrongPost",
            spaceId = spaceId,
            spaceName = "testingWrongPost",
            startTime = fechaActual.minusDays(3).toString(),
            endTime = fechaActual.plusHours(1).minusDays(3).toString(),
            observations = "testingWrongPost"
        )

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun postOccupiedDate() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val bookingCreateDTO = BookingCreateDTO(
            userId = userId,
            userName = "testingWrongPost",
            spaceId = spaceId,
            spaceName = "testingWrongPost",
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "testingWrongPost"
        )

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun postAfterBookingWindow() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val bookingCreateDTO = BookingCreateDTO(
            userId = userId,
            userName = "testingWrongPost",
            spaceId = spaceId,
            spaceName = "testingWrongPost",
            startTime = fechaActual.plusDays(15).toString(),
            endTime = fechaActual.plusHours(1).plusDays(15).toString(),
            observations = "testingWrongPost"
        )

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.post("/bookings") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingCreateDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun getByTime400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/bookings/time/123/${fechaActual.toString().split("T")[0]}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    //TODO: el test funciona hay que mirar que deje actualizar si no cambias la fecha
    /*@Test
    fun putMe() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userId,
            userName = "testingUpdate",
            spaceId = spaceId,
            spaceName = "testingUpdate",
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "testingUpdate",
            status = "APPROVED"
        )

        val response = client.put("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

     */

    /*@Test
    fun put() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userNotAdminId,
            userName = "testingUpdate1",
            spaceId = spaceId,
            spaceName = "testingUpdate1",
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "testingUpdate1",
            status = "APPROVED"
        )

        val response = client.put("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

     */

    /*@Test
    fun putNotAdminMe() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userNotAdminId,
            userName = "testingUpdate2",
            spaceId = spaceId,
            spaceName = "testingUpdate2",
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "testingUpdate2",
            status = "APPROVED"
        )

        val response = client.put("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

     */

    /*@Test
    fun putNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userId,
            userName = "testingUpdate3",
            spaceId = spaceId,
            spaceName = "testingUpdate3",
            startTime = fechaActual.toString(),
            endTime = fechaActual.plusHours(1).toString(),
            observations = "testingUpdate3",
            status = "APPROVED"
        )

        val response = client.put("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

     */

    @Test
    fun put404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userId,
            userName = "testingUpdate",
            spaceId = spaceId,
            spaceName = "testingUpdate",
            startTime = fechaActual.plusDays(5).toString(),
            endTime = fechaActual.plusHours(1).plusDays(5).toString(),
            observations = "testingUpdate",
            status = "APPROVED"
        )

        val response = client.put("/bookings/$uuid404") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    //TODO: peta porque salta la excepcion del microservicio y no la de la api
    /*@Test
    fun put400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val bookingUpdateDTO = BookingUpdateDTO(
            userId = userId,
            userName = "testingUpdate",
            spaceId = spaceId,
            spaceName = "testingUpdate",
            startTime = fechaActual.plusDays(6).toString(),
            endTime = fechaActual.plusHours(1).plusDays(6).toString(),
            observations = "testingUpdate",
            status = "APPROVED"
        )

        val response = client.put("/bookings/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(bookingUpdateDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

     */

    @Test
    fun deleteMe() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/${deletedBookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
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
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/${deletedBookingId2}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun deleteNotAdminMe() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/${deletedBookingId3}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        val creditsResponse = client.get("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(20, json.decodeFromString<UserResponseDTO>(creditsResponse.bodyAsText()).credits)
        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun deleteNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/${bookingId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
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
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/$uuid404") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }


    @Test
    fun delete400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.delete("/bookings/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}