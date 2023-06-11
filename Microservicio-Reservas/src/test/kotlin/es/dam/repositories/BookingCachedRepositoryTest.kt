package es.dam.repositories

import es.dam.exceptions.BookingException
import es.dam.models.Booking
import es.dam.services.cache.BookingCacheImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.litote.kmongo.id.toId
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class BookingCachedRepositoryTest {

    val booking = Booking(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        userId = UUID.randomUUID().toString(),
        spaceId = UUID.randomUUID().toString(),
        status = Booking.Status.PENDING,
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        observations =  "test",
        userName = "test",
        spaceName = "test",
        image = ""
    )

    @MockK
    lateinit var repo: BookingRepositoryImpl

    @SpyK
    var cache = BookingCacheImpl()

    @InjectMockKs
    lateinit var repository: BookingCachedRepository

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAll() = runTest {
        coEvery { repo.findAll() } returns listOf(booking)

        val result = repository.findAll().toList()

        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(booking, result[0]) }
        )
    }

    @Test
    fun findById() = runTest {
        coEvery { repo.findById(any()) } returns booking

        val result = repository.findById(UUID.fromString(booking.uuid))

        assertAll(
            { assertEquals(booking.userName, result.userName) },
            { assertEquals(booking.spaceName, result.spaceName) },
        )
    }

    @Test
    fun findByIdNotFound() = runTest {
        coEvery { repo.findById(any()) } returns null

        val exception = assertFailsWith(BookingException::class) {
            repository.findById(UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166f"))
        }
        assertEquals("No se ha encontrado la reserva con uuid c060c959-8462-4a0f-9265-9af4f54d166f", exception.message)
    }

    @Test
    fun save() = runTest {
        coEvery { repo.save(any()) } returns booking

        val result = repository.save(booking)

        assertAll(
            { assertEquals(booking.userName, result.userName) },
            { assertEquals(booking.spaceName, result.spaceName) },
        )
    }

    @Test
    fun update()  = runTest {
        coEvery { repo.update(any()) } returns booking

        val result = repository.update(booking)

        assertAll(
            { assertEquals(booking.userName, result.userName) },
            { assertEquals(booking.spaceName, result.spaceName) },
        )
    }
    @Test
    fun delete()  = runTest {
        coEvery { repo.delete(any()) } returns true

        val result = repository.delete(UUID.fromString(booking.uuid))

        assertTrue(result)
    }

    @Test
    fun deleteAll()  = runTest {
        coEvery { repo.deleteAll() } returns true

        val result = repository.deleteAll()

        assertTrue(result)
    }
}