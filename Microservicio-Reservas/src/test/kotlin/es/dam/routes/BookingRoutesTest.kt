package es.dam.routes


import es.dam.dto.BookingAllDto
import es.dam.dto.BookingDto
import es.dam.dto.BookingDtoCreate
import es.dam.mappers.toDTO
import es.dam.models.Booking
import es.dam.repositories.BookingRepositoryImpl
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.litote.kmongo.id.toId
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull


@OptIn(ExperimentalCoroutinesApi::class)
@TestClassOrder(ClassOrderer.OrderAnnotation::class)
class BookingRoutesTest {

    val jsonPerso = Json { ignoreUnknownKeys = true }
    val booking = Booking(
        id = ObjectId("645bfcb4021a8675e05afdb2").toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a47").toString(),
        spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b23").toString(),
        status = Booking.Status.PENDING,
        startTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200"),
        endTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200"),
        phone = "123456789",
    )
    val bookingDto = booking.toDTO()
    val bookingDtoCreate = BookingDtoCreate(
        userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a47").toString(),
        spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b23").toString(),
        status = Booking.Status.PENDING.toString(),
        startTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200").toString(),
        endTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200").toString(),
        phone = "123456789",
    )
    val data = BookingAllDto(listOf(bookingDto))


    @OptIn(InternalAPI::class)
    @Order(1)
    @Test
    fun getAll() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)
        val response = client.get("/bookings")

        val responseData = jsonPerso.decodeFromString<BookingAllDto>(response.content.readUTF8Line()!!).data
        val startTimeReduced = responseData[0].startTime.substring(0, 23)
        val endTimeReduced = responseData[0].endTime.substring(0, 23)
        assertAll(
            { assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(1, responseData.size) },
            { assertEquals(responseData[0].id, responseData[0].id) },
            { assertEquals(responseData[0].uuid, responseData[0].uuid) },
            { assertEquals(responseData[0].userId, responseData[0].userId) },
            { assertEquals(responseData[0].spaceId, responseData[0].spaceId) },
            { assertEquals(responseData[0].status, responseData[0].status) },
            { assertEquals(startTimeReduced, responseData[0].startTime.toString()) },
            { assertEquals(endTimeReduced, responseData[0].endTime.toString()) },
            { assertEquals(responseData[0].phone, responseData[0].phone) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    @Order(2)
    fun getById() = testApplication {
        environment { config }
        val response = client.get("/bookings/${booking.uuid}")
        val responseData = jsonPerso.decodeFromString<BookingDto>(response.content.readUTF8Line()!!)
        val startTimeReduced = responseData.startTime.substring(0, 23)
        val endTimeReduced = responseData.endTime.substring(0, 23)
        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(bookingDto.id, responseData.id) },
            { assertEquals(bookingDto.uuid, responseData.uuid) },
            { assertEquals(bookingDto.userId, responseData.userId) },
            { assertEquals(bookingDto.spaceId, responseData.spaceId) },
            { assertEquals(bookingDto.status, responseData.status) },
            { assertEquals(startTimeReduced, responseData.startTime.toString()) },
            { assertEquals(endTimeReduced, responseData.endTime.toString()) },
            { assertEquals(bookingDto.phone, responseData.phone) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun findIdNotFound() = testApplication {
        environment { config }
        val response = client.get("/bookings/1cca337f-9dbc-4e53-8904-58961235b7da")
        val body =  response.content.readUTF8Line()!!
        assertAll(
                {assertEquals(HttpStatusCode.NotFound, response.status)},
                { assertEquals("No se ha encontrado la reserva con el id: 1cca337f-9dbc-4e53-8904-58961235b7da", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    @Order(3)
    fun failWithId() = testApplication {
        environment { config }
        val response = client.get("/bookings/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
                {assertEquals(HttpStatusCode.BadRequest, response.status)},
                { assertEquals("El id debe ser un id válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    @Order(3)
    fun save() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/bookings") {
            contentType(ContentType.Application.Json)
            setBody(bookingDtoCreate)
        }
        val responseData = jsonPerso.decodeFromString<BookingDto>(response.content.readUTF8Line()!!)
        assertAll(
            {assertEquals(HttpStatusCode.Created, response.status)},
            { assertNotNull(responseData.id) },
            { assertNotNull(responseData.uuid) },
            { assertEquals(bookingDtoCreate.userId, responseData.userId) },
            { assertEquals(bookingDtoCreate.spaceId, responseData.spaceId) },
            { assertEquals(bookingDtoCreate.status, responseData.status) },
            { assertEquals(bookingDtoCreate.startTime,responseData.startTime) },
            { assertEquals(bookingDtoCreate.endTime, responseData.endTime) },
            { assertEquals(bookingDtoCreate.phone, responseData.phone) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    @Order(4)
    fun update() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.put("/bookings/${booking.uuid}") {
            contentType(ContentType.Application.Json)
            setBody(bookingDtoCreate)
        }
        val responseData = jsonPerso.decodeFromString<BookingDto>(response.content.readUTF8Line()!!)
        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(bookingDto.id, responseData.id) },
            { assertEquals(bookingDto.uuid, responseData.uuid) },
            { assertEquals(bookingDtoCreate.userId, responseData.userId) },
            { assertEquals(bookingDtoCreate.spaceId, responseData.spaceId) },
            { assertEquals(bookingDtoCreate.status, responseData.status) },
            { assertEquals(bookingDtoCreate.startTime,responseData.startTime) },
            { assertEquals(bookingDtoCreate.endTime, responseData.endTime) },
            { assertEquals(bookingDtoCreate.phone, responseData.phone) }
        )
    }

    @Test
    @Order(5)
    fun updateNotFound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.put("/bookings/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
            setBody(bookingDtoCreate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
        )
    }

    @Test
    fun updateBadRequest() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.put("/bookings/234") {
            contentType(ContentType.Application.Json)
            setBody(bookingDtoCreate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }


    @Test
    @Order(6)
    fun delete() = testApplication {
        environment { config }
        val response = client.delete("/bookings/${booking.uuid}")
        assertAll(
            {assertEquals(HttpStatusCode.NoContent, response.status)},
        )
    }

    @Test
    @Order(7)
    fun deleteBadRequest() = testApplication {
        environment { config }
        val response = client.delete("/bookings/2")
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init(): Unit = runTest {
            val booking = Booking(
                id = ObjectId("645bfcb4021a8675e05afdb2").toId(),
                uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
                userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a47").toString(),
                spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b23").toString(),
                status = Booking.Status.PENDING,
                startTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200"),
                endTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200"),
                phone = "123456789",
            )
            BookingRepositoryImpl().deleteAll()
            BookingRepositoryImpl().save(booking)
        }
    }
}