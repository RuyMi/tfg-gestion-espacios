package es.dam.repositories

import es.dam.exceptions.SpaceException
import es.dam.models.Space
import es.dam.services.cache.SpaceCacheImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.litote.kmongo.id.toId
import java.util.*
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class SpaceCachedRepositoryTest {

    val space = Space(
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

    @MockK
    lateinit var repo: SpaceRepositoryImpl

    @SpyK
    var cache = SpaceCacheImpl()

    @InjectMockKs
    lateinit var repository: SpaceCachedRepository

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns listOf(space)

        val result = repository.findAll().toList()

        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(space, result[0]) }
        )
    }

    @Test
    fun findById() = runTest {
        coEvery { repo.findById(any()) } returns space

        val result = repository.findById(UUID.fromString(space.uuid))

        assertAll(
            { assertEquals(space.name, result.name) },
            { assertEquals(space.bookingWindow, result.bookingWindow) },
        )
    }

    @Test
    fun findByIdNotFound() = runTest {
        coEvery { repo.findById(any()) } returns null

        val exception = assertFailsWith(SpaceException::class) {
            repository.findById(UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166f"))
        }
        assertEquals("No se ha encontrado el espacio con uuid c060c959-8462-4a0f-9265-9af4f54d166f", exception.message)
    }

    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns space

        val result = repository.save(space)

        assertAll(
            { assertEquals(space.name, result.name) },
            { assertEquals(space.bookingWindow, result.bookingWindow) },
        )
    }

    @Test
    fun update()  = runTest {
        coEvery { repo.update(any()) } returns space

        val result = repository.update(space)

        assertAll(
            { assertEquals(space.name, result.name) },
            { assertEquals(space.bookingWindow, result.bookingWindow) },
        )
    }
    @Test
    fun delete()  = runTest {
        coEvery { repo.delete(any()) } returns true

        val result = repository.delete(UUID.fromString(space.uuid))

        assertTrue(result)
    }

    @Test
    fun deleteAll()  = runTest {
        coEvery { repo.deleteAll() } returns true

        val result = repository.deleteAll()

        assertTrue(result)
    }
}