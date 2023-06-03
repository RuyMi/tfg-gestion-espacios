package es.dam.services.spaces

import es.dam.exceptions.SpaceException
import es.dam.models.Space
import es.dam.repositories.SpaceRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.litote.kmongo.id.toId
import java.util.*
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class SpaceServiceImplTest {

    @MockK
    lateinit var spaceRepository : SpaceRepositoryImpl

    @InjectMockKs
    lateinit var spaceService : SpaceServiceImpl

    val space = Space(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        name = "test",
        image = null,
        price = 1,
        isReservable = false,
        requiresAuthorization = false,
        authorizedRoles = setOf(Space.UserRole.USER),
        bookingWindow = 10
    )

    val spaceReservable = Space(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        name = "test",
        image = null,
        price = 1,
        isReservable = true,
        requiresAuthorization = false,
        authorizedRoles = setOf(Space.UserRole.USER),
        bookingWindow = 10
    )


    @Test
    fun findAll() = runTest {
        coEvery { spaceRepository.findAll() } returns listOf(space)

        val result = spaceService.getAllSpaces()
        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(space, result[0]) },
            { assertEquals(space.id, result[0].id) },
            { assertEquals(space.uuid, result[0].uuid) },
            { assertEquals(space.name, result[0].name) },
            { assertEquals(space.image, result[0].image) },
            { assertEquals(space.price, result[0].price) },
            { assertEquals(space.isReservable, result[0].isReservable)},
            { assertEquals(space.requiresAuthorization, result[0].requiresAuthorization)},
            { assertEquals(space.authorizedRoles, result[0].authorizedRoles) },
            { assertEquals(space.bookingWindow, result[0].bookingWindow) }
        )
    }

    @Test
    fun findById() = runTest {
        coEvery { spaceRepository.findById(UUID.fromString(space.uuid)) } returns space

        val result = spaceService.getSpaceById(space.uuid)

        assertAll(
            { assertEquals(space, result) },
            { assertEquals(space.id, result.id) },
            { assertEquals(space.uuid, result.uuid) },
            { assertEquals(space.name, result.name) },
            { assertEquals(space.image, result.image) },
            { assertEquals(space.price, result.price) },
            { assertEquals(space.isReservable, result.isReservable)},
            { assertEquals(space.requiresAuthorization, result.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, result.authorizedRoles) },
            { assertEquals(space.bookingWindow, result.bookingWindow) }
        )
    }

    @Test
    fun notFoundById() = runTest {
        coEvery { spaceRepository.findById(UUID.fromString(space.uuid)) } throws SpaceException("No se ha encontrado el espacio con uuid ${space.uuid}")

        val exception = assertFailsWith(SpaceException::class) {
            spaceService.getSpaceById(space.uuid)
        }
        assertEquals("No se ha encontrado el espacio con uuid ${space.uuid}", exception.message)
    }

    @Test
    fun save() = runTest {
        coEvery { spaceRepository.save(any()) } returns space

        val result = spaceService.createSpace(space)

        assertAll(
            { assertEquals(space, result) },
            { assertEquals(space.id, result.id) },
            { assertEquals(space.uuid, result.uuid) },
            { assertEquals(space.name, result.name) },
            { assertEquals(space.image, result.image) },
            { assertEquals(space.price, result.price) },
            { assertEquals(space.isReservable, result.isReservable)},
            { assertEquals(space.requiresAuthorization, result.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, result.authorizedRoles) },
            { assertEquals(space.bookingWindow, result.bookingWindow) }
        )
    }

    @Test
    fun failSave() = runTest {
        coEvery { spaceRepository.save(any()) } throws SpaceException("Error al guardar el espacio con uuid ${space.uuid}")

        val exception = assertFailsWith(SpaceException::class) {
            spaceService.createSpace(space)
        }
        assertEquals("Error al guardar el espacio con uuid ${space.uuid}", exception.message)
    }
    @Test
    fun update() = runTest {
        coEvery { spaceRepository.update(space) } returns space
        coEvery { spaceRepository.findById(UUID.fromString(space.uuid)) } returns space

        val result = spaceService.updateSpace(space, space.uuid)

        assertAll(
            { assertEquals(space, result) },
            { assertEquals(space.id, result.id) },
            { assertEquals(space.uuid, result.uuid) },
            { assertEquals(space.name, result.name) },
            { assertEquals(space.image, result.image) },
            { assertEquals(space.price, result.price) },
            { assertEquals(space.isReservable, result.isReservable)},
            { assertEquals(space.requiresAuthorization, result.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, result.authorizedRoles) },
            { assertEquals(space.bookingWindow, result.bookingWindow) }
        )
    }

    @Test
    fun failUpdate() = runTest {
        coEvery { spaceRepository.findById(UUID.fromString(space.uuid)) } returns space
        coEvery { spaceRepository.update(space) } throws SpaceException("No se ha encontrado el espacio con uuid ${space.uuid}")

        val exception = assertFailsWith(SpaceException::class) {
            spaceService.updateSpace(space, space.uuid)
        }
        assertEquals("No se ha encontrado el espacio con uuid ${space.uuid}", exception.message)
    }

    @Test
    fun findByName() = runTest {
        coEvery { spaceRepository.findByName(space.name) } returns space

        val response = spaceService.getSpaceByName(space.name)

        assertAll(
            { assertEquals(space, response) },
            { assertEquals(space.id, response.id) },
            { assertEquals(space.uuid, response.uuid) },
            { assertEquals(space.name, response.name) },
            { assertEquals(space.image, response.image) },
            { assertEquals(space.price, response.price) },
            { assertEquals(space.isReservable, response.isReservable)},
            { assertEquals(space.requiresAuthorization, response.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, response.authorizedRoles) },
            { assertEquals(space.bookingWindow, response.bookingWindow) }
        )
    }

    @Test
    fun failFindByName() = runTest {
        coEvery { spaceRepository.findByName(space.name) } throws SpaceException("No se ha encontrado el espacio con nombre ${space.name}")

        val exception = assertFailsWith(SpaceException::class) {
            spaceService.getSpaceByName(space.name)
        }
        assertEquals("No se ha encontrado el espacio con nombre ${space.name}", exception.message)
    }

    @Test
    fun findAllReservables() = runTest {
        coEvery { spaceRepository.findAllReservables(true) } returns listOf(spaceReservable)

        val result = spaceService.getAllSpacesReservables(true)
        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(spaceReservable, result[0]) },
            { assertEquals(spaceReservable.id, result[0].id) },
            { assertEquals(spaceReservable.uuid, result[0].uuid) },
            { assertEquals(spaceReservable.name, result[0].name) },
            { assertEquals(spaceReservable.image, result[0].image) },
            { assertEquals(spaceReservable.price, result[0].price) },
            { assertEquals(spaceReservable.isReservable, result[0].isReservable)},
            { assertEquals(spaceReservable.requiresAuthorization, result[0].requiresAuthorization)},
            { assertEquals(spaceReservable.authorizedRoles, result[0].authorizedRoles) },
            { assertEquals(spaceReservable.bookingWindow, result[0].bookingWindow) }
        )
    }

    @Test
    fun delete() = runTest {
        coEvery { spaceRepository.delete(UUID.fromString(space.uuid)) } returns true

        val result = spaceService.deleteSpace(space.uuid)

        assertTrue(result)
    }

    @Test
    fun failDelete() = runTest {
        coEvery { spaceRepository.delete(UUID.fromString(space.uuid))} returns false

        val result = spaceService.deleteSpace(space.uuid)

        assertFalse(result)
    }
}