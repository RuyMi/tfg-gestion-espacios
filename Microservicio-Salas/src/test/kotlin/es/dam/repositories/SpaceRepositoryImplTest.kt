package es.dam.repositories

import es.dam.models.Space
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.litote.kmongo.id.toId
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
class SpaceRepositoryImplTest {

    val repository = SpaceRepositoryImpl()

    val space = Space(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        name = "test1",
        image = null,
        price = 1,
        isReservable = false,
        requiresAuthorization = false,
        authorizedRoles = setOf(Space.UserRole.USER),
        bookingWindow = 10
    )

    val spaceReservable = Space(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166d").toString(),
        name = "test2",
        image = null,
        price = 1,
        isReservable = true,
        requiresAuthorization = false,
        authorizedRoles = setOf(Space.UserRole.USER),
        bookingWindow = 10
    )


    @BeforeEach
    fun setUp() = runTest {
        repository.save(space)
        repository.save(spaceReservable)
    }

    @AfterEach
    fun tearDown() = runTest {
        repository.delete(UUID.fromString(space.uuid))
        repository.delete(UUID.fromString(spaceReservable.uuid))
    }

    @Test
    fun findAll() = runTest {
        val spaces = repository.findAll()

        assertTrue(spaces.size >= 2)
    }

    @Test
    fun findById() = runTest {
        val response = repository.findById(UUID.fromString(space.uuid))

        assertAll(
            { assertEquals(space, response) },
            { assertEquals(space.id, response?.id) },
            { assertEquals(space.uuid, response?.uuid) },
            { assertEquals(space.name, response?.name) },
            { assertEquals(space.image, response?.image) },
            { assertEquals(space.price, response?.price) },
            { assertEquals(space.isReservable, response?.isReservable)},
            { assertEquals(space.requiresAuthorization, response?.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, response?.authorizedRoles) },
            { assertEquals(space.bookingWindow, response?.bookingWindow) }
        )
    }

    @Test
    fun save() = runTest {
        repository.delete(UUID.fromString(space.uuid))
        val response = repository.save(space)

        assertAll(
            { assertEquals(space, response) },
            { assertEquals(space.id, response?.id) },
            { assertEquals(space.uuid, response?.uuid) },
            { assertEquals(space.name, response?.name) },
            { assertEquals(space.image, response?.image) },
            { assertEquals(space.price, response?.price) },
            { assertEquals(space.isReservable, response?.isReservable)},
            { assertEquals(space.requiresAuthorization, response?.requiresAuthorization)},
            { assertEquals(space.authorizedRoles, response?.authorizedRoles) },
            { assertEquals(space.bookingWindow, response?.bookingWindow) }
        )

    }

    @Test
    fun update() = runTest {
        val response = space.copy(
            name = "testUpdate",
            bookingWindow = 10
        )
        val updatedResponse = repository.update(response)

        assertAll(
            { assertEquals(updatedResponse, response) },
            { assertEquals(updatedResponse?.id, response.id) },
            { assertEquals(updatedResponse?.uuid, response.uuid) },
            { assertEquals(updatedResponse?.name, response.name) },
            { assertEquals(updatedResponse?.image, response.image) },
            { assertEquals(updatedResponse?.price, response.price) },
            { assertEquals(updatedResponse?.isReservable, response.isReservable)},
            { assertEquals(updatedResponse?.requiresAuthorization, response.requiresAuthorization)},
            { assertEquals(updatedResponse?.authorizedRoles, response.authorizedRoles) },
            { assertEquals(updatedResponse?.bookingWindow, response.bookingWindow) }
        )
    }

    @Test
    fun delete() = runTest {
        val deleted = repository.delete(UUID.fromString(space.uuid))
        assertTrue(deleted)

        repository.save(space)
    }

    @Test
    fun findByName() = runTest {
        val response = repository.findByName(spaceReservable.name)

        assertAll(
            { assertEquals(spaceReservable, response) },
            { assertEquals(spaceReservable.id, response.id) },
            { assertEquals(spaceReservable.uuid, response.uuid) },
            { assertEquals(spaceReservable.name, response.name) },
            { assertEquals(spaceReservable.image, response.image) },
            { assertEquals(spaceReservable.price, response.price) },
            { assertEquals(spaceReservable.isReservable, response.isReservable)},
            { assertEquals(spaceReservable.requiresAuthorization, response.requiresAuthorization)},
            { assertEquals(spaceReservable.authorizedRoles, response.authorizedRoles) },
            { assertEquals(spaceReservable.bookingWindow, response.bookingWindow) }
        )
    }

    @Test
    fun findAllReservables() = runTest {
        val spaces = repository.findAllReservables(true)

        assertTrue(spaces.size >= 1)
    }
}