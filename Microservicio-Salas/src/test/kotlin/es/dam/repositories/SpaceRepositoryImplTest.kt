package es.dam.repositories

import es.dam.models.Space
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
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


    @BeforeEach
    fun setUp() = runTest {
        repository.deleteAll()
        repository.save(space)
        repository.save(spaceReservable)
    }

    @Test
    fun findAll() = runTest {
        val spaces = repository.findAll()

        assertAll(
            { assertEquals(2, spaces.size) },
            { assertEquals(space, spaces[0]) },
            { assertEquals(space.id, spaces[0].id) },
            { assertEquals(space.uuid, spaces[0].uuid) },
            { assertEquals(space.name, spaces[0].name) },
            { assertEquals(space.image, spaces[0].image) },
            { assertEquals(space.price, spaces[0].price) },
            { assertEquals(space.isReservable, spaces[0].isReservable)},
            { assertEquals(space.requiresAuthorization, spaces[0].requiresAuthorization)},
            { assertEquals(space.authorizedRoles, spaces[0].authorizedRoles) },
            { assertEquals(space.bookingWindow, spaces[0].bookingWindow) }
        )
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
    }

    @Test
    fun findByName() = runTest {
        val response = repository.findByName(space.name)

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
    fun findAllReservables() = runTest {
        val spaces = repository.findAllReservables(true)

        assertAll(
            { assertEquals(1, spaces.size) },
            { assertEquals(spaceReservable, spaces[0]) },
            { assertEquals(spaceReservable.id, spaces[0].id) },
            { assertEquals(spaceReservable.uuid, spaces[0].uuid) },
            { assertEquals(spaceReservable.name, spaces[0].name) },
            { assertEquals(spaceReservable.image, spaces[0].image) },
            { assertEquals(spaceReservable.price, spaces[0].price) },
            { assertEquals(spaceReservable.isReservable, spaces[0].isReservable)},
            { assertEquals(spaceReservable.requiresAuthorization, spaces[0].requiresAuthorization)},
            { assertEquals(spaceReservable.authorizedRoles, spaces[0].authorizedRoles) },
            { assertEquals(spaceReservable.bookingWindow, spaces[0].bookingWindow) }
        )
    }

    @Test
    fun deleteAll() = runTest {
        val deleted = repository.deleteAll()
        assertTrue(deleted)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUpAll(): Unit = runTest {
            val repository = SpaceRepositoryImpl()
            repository.deleteAll()
        }
    }
}