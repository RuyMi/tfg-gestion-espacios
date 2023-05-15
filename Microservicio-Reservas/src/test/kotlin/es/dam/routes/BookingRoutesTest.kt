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
        userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a50").toString(),
        spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b24").toString(),
        status = Booking.Status.PENDING.toString(),
        startTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200").toString(),
        endTime = LocalDateTime.parse("2023-05-10T22:23:23.542295200").toString(),
        phone = "123456789",
    )
    val data = BookingAllDto(listOf(bookingDto))


    @OptIn(InternalAPI::class)
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
    fun getByStatus() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)

        val response = client.get("/bookings/status/${booking.status}")
        val responseData = jsonPerso.decodeFromString<BookingAllDto>(response.content.readUTF8Line()!!).data
        val startTimeReduced = responseData[0].startTime.substring(0, 23)
        val endTimeReduced = responseData[0].endTime.substring(0, 23)

        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(1, responseData.size) },
            { assertEquals(responseData[0].id, responseData[0].id) },
            { assertEquals(responseData[0].uuid, responseData[0].uuid) },
            { assertEquals(responseData[0].userId, responseData[0].userId) },
            { assertEquals(responseData[0].spaceId, responseData[0].spaceId) },
            { assertEquals(responseData[0].status, responseData[0].status) },
            { assertEquals(startTimeReduced, responseData[0].startTime) },
            { assertEquals(endTimeReduced, responseData[0].endTime) },
            { assertEquals(responseData[0].phone, responseData[0].phone) }
        )

    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByStatusNotFound() = testApplication {
        environment { config }
        val statusTest = Booking.Status.APPROVED
        val response = client.get("/bookings/status/${statusTest}")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado ninguna reserva con el estado: $statusTest", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByStatusBadRequest() = testApplication {
        environment { config }
        val response = client.get("/bookings/status/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("El estado debe ser un estado válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByDate() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)
        val fechaTest = booking.startTime.toLocalDate()
        val response = client.get("/bookings/time/${booking.spaceId}/$fechaTest")
        val responseData = jsonPerso.decodeFromString<BookingAllDto>(response.content.readUTF8Line()!!).data
        val startTimeReduced = responseData[0].startTime.substring(0, 23)
        val endTimeReduced = responseData[0].endTime.substring(0, 23)

        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(1, responseData.size) },
            { assertEquals(responseData[0].id, responseData[0].id) },
            { assertEquals(responseData[0].uuid, responseData[0].uuid) },
            { assertEquals(responseData[0].userId, responseData[0].userId) },
            { assertEquals(responseData[0].spaceId, responseData[0].spaceId) },
            { assertEquals(responseData[0].status, responseData[0].status) },
            { assertEquals(startTimeReduced, responseData[0].startTime) },
            { assertEquals(endTimeReduced, responseData[0].endTime) },
            { assertEquals(responseData[0].phone, responseData[0].phone) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByDateNotFound() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)
        val fechaTest = LocalDateTime.parse("2023-07-10T22:23:23.542295200").toLocalDate()
        val response = client.get("/bookings/time/${booking.spaceId}/$fechaTest")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado ninguna reserva para la sala con uuid: ${booking.spaceId} cuya fecha de reserva sea: $fechaTest", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByDateNotFoundUuid() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)
        val uuidTest = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a33").toString()
        val response = client.get("/bookings/time/$uuidTest/$${booking.startTime}")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            { assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado ninguna sala con el uuid: $uuidTest", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByDateBadRequestUuid() = testApplication {
        environment { config }
        val fechaTest = booking.startTime.toLocalDate()
        val response = client.get("/bookings/time/123/$fechaTest")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("El id debe ser un id válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByDateBadRequestDate() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)
        val response = client.get("/bookings/time/${booking.spaceId}/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("La fecha debe tener un formato válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getBySpace() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)

        val response = client.get("/bookings/space/${booking.spaceId}")
        val responseData = jsonPerso.decodeFromString<BookingAllDto>(response.content.readUTF8Line()!!).data
        val startTimeReduced = responseData[0].startTime.substring(0, 23)
        val endTimeReduced = responseData[0].endTime.substring(0, 23)

        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(1, responseData.size) },
            { assertEquals(responseData[0].id, responseData[0].id) },
            { assertEquals(responseData[0].uuid, responseData[0].uuid) },
            { assertEquals(responseData[0].userId, responseData[0].userId) },
            { assertEquals(responseData[0].spaceId, responseData[0].spaceId) },
            { assertEquals(responseData[0].status, responseData[0].status) },
            { assertEquals(startTimeReduced, responseData[0].startTime) },
            { assertEquals(endTimeReduced, responseData[0].endTime) },
            { assertEquals(responseData[0].phone, responseData[0].phone) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getBySpaceNotFound() = testApplication {
        environment { config }
        val uuidTest = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166d").toString()
        val response = client.get("/bookings/space/$uuidTest")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado ninguna reserva con el id de espacio: $uuidTest", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getBySpaceBadRequest() = testApplication {
        environment { config }
        val response = client.get("/bookings/space/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("El id debe ser un id válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByUser() = testApplication {
        environment { config }
        BookingRepositoryImpl().save(booking)

        val response = client.get("/bookings/user/${booking.userId}")
        val responseData = jsonPerso.decodeFromString<BookingAllDto>(response.content.readUTF8Line()!!).data
        val startTimeReduced = responseData[0].startTime.substring(0, 23)
        val endTimeReduced = responseData[0].endTime.substring(0, 23)

        assertAll(
            {assertEquals(HttpStatusCode.OK, response.status)},
            { assertEquals(1, responseData.size) },
            { assertEquals(responseData[0].id, responseData[0].id) },
            { assertEquals(responseData[0].uuid, responseData[0].uuid) },
            { assertEquals(responseData[0].userId, responseData[0].userId) },
            { assertEquals(responseData[0].spaceId, responseData[0].spaceId) },
            { assertEquals(responseData[0].status, responseData[0].status) },
            { assertEquals(startTimeReduced, responseData[0].startTime) },
            { assertEquals(endTimeReduced, responseData[0].endTime) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByUserNotFound() = testApplication {
        environment { config }
        val response = client.get("/bookings/user/b4443487-b2cc-48b6-af53-0820be683b24")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado ninguna reserva con el id de usuario: b4443487-b2cc-48b6-af53-0820be683b24", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByUserBadRequest() = testApplication {
        environment { config }
        val response = client.get("/bookings/user/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("El id debe ser un id válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
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
    fun delete() = testApplication {
        environment { config }
        val response = client.delete("/bookings/${booking.uuid}")
        assertAll(
            {assertEquals(HttpStatusCode.NoContent, response.status)},
        )
    }

    @Test
    fun deleteBadRequest() = testApplication {
        environment { config }
        val response = client.delete("/bookings/2")
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }

    @Test
    fun deleteNotFound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.delete("/bookings/1cca337f-9dbc-4e53-8904-58961235b7da")
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
        )
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init(): Unit = runTest {
            val booking = Booking(
                id = ObjectId("645bfcb4021a8675e05afdb2").toId(),
                uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
                userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a48").toString(),
                spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b25").toString(),
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