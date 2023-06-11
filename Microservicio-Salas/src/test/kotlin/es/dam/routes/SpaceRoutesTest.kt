package es.dam.routes

import com.mongodb.assertions.Assertions.assertTrue
import es.dam.dto.SpaceCreateDTO
import es.dam.dto.SpaceDTO
import es.dam.dto.SpaceDataDTO
import es.dam.dto.SpaceUpdateDTO
import es.dam.mappers.toSpaceDto
import es.dam.models.Space
import es.dam.repositories.SpaceRepositoryImpl
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
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.litote.kmongo.id.toId
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class SpaceRoutesTest {

    val jsonPerso = Json { ignoreUnknownKeys = true }
    val space = Space(
        id = ObjectId("645bfcb4021a8675e05afdb2").toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        name = "test1",
        image = "image",
        price = 1,
        isReservable = true,
        requiresAuthorization = true,
        authorizedRoles = setOf(Space.UserRole.USER),
        bookingWindow = 10
    )
    val spaceDto = space.toSpaceDto()
    val spaceDtoCreate = SpaceCreateDTO(
        name = "test2",
        image = "image",
        price = 20,
        isReservable = true,
        requiresAuthorization = true,
        authorizedRoles = setOf(Space.UserRole.USER.toString()),
        bookingWindow = 10
    )

    val spaceDtoUpdate = SpaceUpdateDTO(
        name = "test1",
        image = "imageUpdated",
        isReservable = true,
        requiresAuthorization = true,
        authorizedRoles = setOf(Space.UserRole.USER.toString()),
        bookingWindow = 10,
        price = 1
    )
    val data = SpaceDataDTO(
        listOf(spaceDto)
    )

    @OptIn(InternalAPI::class)
    @Test
    fun getAll() = testApplication {
        environment {
            config
        }

        val response = client.get("/spaces")

        val responseData = jsonPerso.decodeFromString<SpaceDataDTO>(response.content.readUTF8Line()!!).data
        assertTrue(responseData.isNotEmpty())
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getOne() = testApplication {
        environment {
            config
        }

        val response = client.get("/spaces/${space.uuid}")

        val responseData = jsonPerso.decodeFromString<SpaceDTO>(response.content.readUTF8Line()!!)
        assertAll(
            { assertEquals(spaceDto, responseData) },
            { assertEquals(spaceDto.id, responseData.id) },
            { assertEquals(spaceDto.uuid, responseData.uuid) },
            { assertEquals(spaceDto.name, responseData.name) },
            { assertEquals(spaceDto.image, responseData.image) },
            { assertEquals(spaceDto.price, responseData.price) },
            { assertEquals(spaceDto.isReservable, responseData.isReservable) },
            { assertEquals(spaceDto.requiresAuthorization, responseData.requiresAuthorization) },
            { assertEquals(spaceDto.authorizedRoles, responseData.authorizedRoles) },
            { assertEquals(spaceDto.bookingWindow, responseData.bookingWindow) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun findIdNotFound() = testApplication {
        environment { config }
        val response = client.get("/spaces/1cca337f-9dbc-4e53-8904-58961235b7da")
        val responseData = response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
            { assertEquals("No se ha encontrado el espacio con el uuid: 1cca337f-9dbc-4e53-8904-58961235b7da", responseData) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun failWithId() = testApplication {
        environment { config }
        val response = client.get("/spaces/123")
        val body =  response.content.readUTF8Line()!!
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
            { assertEquals("El uuid debe ser un uuid válido", body) }
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun create() = testApplication {
        environment {
            config
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/spaces") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoCreate)
        }

        val responseData = jsonPerso.decodeFromString<SpaceDTO>(response.content.readUTF8Line()!!)
        assertAll(
            { assertNotNull(responseData.id) },
            { assertNotNull(responseData.uuid) }
        )

        client.delete("/spaces/${responseData.uuid}") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoCreate)
        }
    }

    @OptIn(InternalAPI::class)
    @Test
    fun update() = testApplication {
        environment {
            config
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.put("/spaces/${space.uuid}") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
        }

        val responseData = jsonPerso.decodeFromString<SpaceDTO>(response.content.readUTF8Line()!!)
        assertAll(
            { assertEquals(spaceDto.id, responseData.id) },
            { assertEquals(spaceDto.uuid, responseData.uuid) },
            { assertEquals(spaceDtoUpdate.name, responseData.name) },
            { assertEquals(spaceDtoUpdate.image, responseData.image) },
            { assertEquals(spaceDtoUpdate.price, responseData.price) },
            { assertEquals(spaceDtoUpdate.isReservable, responseData.isReservable) },
            { assertEquals(spaceDtoUpdate.requiresAuthorization, responseData.requiresAuthorization) },
            { assertEquals(spaceDtoUpdate.authorizedRoles, responseData.authorizedRoles) },
            { assertEquals(spaceDtoUpdate.bookingWindow, responseData.bookingWindow) }
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
        val response = client.put("/spaces/${UUID.randomUUID()}") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
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
        val response = client.put("/spaces/234") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun delete() = testApplication {
        environment {
            config
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/spaces") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoCreate)
        }

        val responseData = jsonPerso.decodeFromString<SpaceDTO>(response.content.readUTF8Line()!!)

        val delete = client.delete("/spaces/${responseData.uuid}") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoCreate)
        }

        assertAll(
            { assertEquals(HttpStatusCode.NoContent, delete.status) },
        )
    }

    @Test
    fun deleteBadRequest() = testApplication {
        environment { config }
        val response = client.delete("/spaces/2")
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }

    @Test
    fun deleteNotfound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.delete("/spaces/1cca337f-9dbc-4e53-8904-58961235b7da")
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getIsReserved() = testApplication {
        environment {
            config
        }

        val response = client.get("/spaces/reservables/${space.isReservable}")

        val responseData = jsonPerso.decodeFromString<SpaceDataDTO>(response.content.readUTF8Line()!!).data
        assertTrue(responseData.isNotEmpty())

    }

    @Test
    fun getIsReservedNotFound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/spaces/reservables/false") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
        )
    }

    @Test
    fun getIsReservedBadRequest() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/spaces/reservables/test") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.BadRequest, response.status)},
        )
    }

    @OptIn(InternalAPI::class)
    @Test
    fun getByName() = testApplication {
        environment {
            config
        }

        val response = client.get("/spaces/nombre/${space.name}")

        val responseData = jsonPerso.decodeFromString<SpaceDTO>(response.content.readUTF8Line()!!)
        assertAll(
            { assertEquals(spaceDto.id, responseData.id) },
            { assertEquals(spaceDto.uuid, responseData.uuid) },
            { assertEquals(spaceDto.name, responseData.name) },
            { assertEquals(spaceDto.price, responseData.price) },
            { assertEquals(spaceDto.isReservable, responseData.isReservable) },
            { assertEquals(spaceDto.requiresAuthorization, responseData.requiresAuthorization) },
            { assertEquals(spaceDto.authorizedRoles, responseData.authorizedRoles) },
            { assertEquals(spaceDto.bookingWindow, responseData.bookingWindow) }
        )
    }

    @Test
    fun getByNameNotFound() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.get("/spaces/nombre/test1234") {
            contentType(ContentType.Application.Json)
            setBody(spaceDtoUpdate)
        }
        assertAll(
            {assertEquals(HttpStatusCode.NotFound, response.status)},
        )
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init(): Unit = runTest {
            val space = Space(
                id = ObjectId("645bfcb4021a8675e05afdb2").toId(),
                uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
                name = "test1",
                image = "image",
                price = 1,
                isReservable = true,
                requiresAuthorization = true,
                authorizedRoles = setOf(Space.UserRole.USER),
                bookingWindow = 10
            )
            SpaceRepositoryImpl().save(space)
        }

        @JvmStatic
        @AfterAll
        fun tearDown(): Unit = runTest {
            SpaceRepositoryImpl().delete(UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c"))
        }
    }


}