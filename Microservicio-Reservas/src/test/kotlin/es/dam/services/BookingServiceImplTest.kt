package es.dam.services

import es.dam.exceptions.BookingException
import es.dam.models.Booking
import es.dam.repositories.BookingRepository
import es.dam.repositories.BookingRepositoryImpl
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.litote.kmongo.id.toId
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class BookingServiceImplTest {

    @MockK
    lateinit var bookingRepository : BookingRepositoryImpl

    @InjectMockKs
    lateinit var bookingService : BookingServiceImpl

    val booking = Booking(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        userId = UUID.fromString("4484ea54-18aa-48a7-b5ed-a46bdbf45a47").toString(),
        spaceId = UUID.fromString("b4443487-b2cc-48b6-af53-0820be683b23").toString(),
        status = Booking.Status.PENDING,
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        phone = "123456789",
    )


    @Test
    fun findAll() = runTest {
        coEvery { bookingRepository.findAll() } returns listOf(booking)

        val result = bookingService.findAll()
        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(booking, result[0]) },
            { assertEquals(booking.id, result[0].id) },
            { assertEquals(booking.uuid, result[0].uuid) },
            { assertEquals(booking.userId, result[0].userId) },
            { assertEquals(booking.spaceId, result[0].spaceId) },
            { assertEquals(booking.status, result[0].status) },
            { assertEquals(booking.startTime, result[0].startTime) },
            { assertEquals(booking.endTime, result[0].endTime) },
            { assertEquals(booking.phone, result[0].phone) },
        )
    }

    @Test
     fun findAllStatus() = runTest {
        coEvery { bookingRepository.findAllStatus(Booking.Status.PENDING) } returns listOf(booking)

        val result = bookingService.findAllStatus(Booking.Status.PENDING)

        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(booking, result[0]) },
            { assertEquals(booking.id, result[0].id) },
            { assertEquals(booking.uuid, result[0].uuid) },
            { assertEquals(booking.userId, result[0].userId) },
            { assertEquals(booking.spaceId, result[0].spaceId) },
            { assertEquals(booking.status, result[0].status) },
            { assertEquals(booking.startTime, result[0].startTime) },
            { assertEquals(booking.endTime, result[0].endTime) },
            { assertEquals(booking.phone, result[0].phone) },
        )
    }

    @Test
    fun findById() = runTest {
        coEvery { bookingRepository.findById(UUID.fromString(booking.uuid)) } returns booking

        val result = bookingService.findById(booking.uuid)

        assertAll(
            { assertEquals(booking, result) },
            { assertEquals(booking.id, result.id) },
            { assertEquals(booking.uuid, result.uuid) },
            { assertEquals(booking.userId, result.userId) },
            { assertEquals(booking.spaceId, result.spaceId) },
            { assertEquals(booking.status, result.status) },
            { assertEquals(booking.startTime, result.startTime) },
            { assertEquals(booking.endTime, result.endTime) },
            { assertEquals(booking.phone, result.phone) }
        )
    }

    @Test
    fun notFoundById() = runTest {
        coEvery { bookingRepository.findById(UUID.fromString(booking.uuid)) } throws BookingException("The booking with userId ${booking.uuid} does not exist")

        val exception = assertFailsWith(BookingException::class) {
            bookingService.findById(booking.uuid)
        }
        assertEquals("The booking with userId ${booking.uuid} does not exist", exception.message)
    }

    @Test
    fun findBySpaceId() = runTest {
        coEvery { bookingRepository.findBySpaceId(UUID.fromString(booking.spaceId)) } returns listOf(booking)

        val result = bookingService.findBySpaceId(booking.spaceId)

        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(booking, result[0]) },
            { assertEquals(booking.id, result[0].id) },
            { assertEquals(booking.uuid, result[0].uuid) },
            { assertEquals(booking.userId, result[0].userId) },
            { assertEquals(booking.spaceId, result[0].spaceId) },
            { assertEquals(booking.status, result[0].status) },
            { assertEquals(booking.startTime, result[0].startTime) },
            { assertEquals(booking.endTime, result[0].endTime) },
            { assertEquals(booking.phone, result[0].phone) })
    }

    @Test
    fun notFoundBySpaceId() = runTest {
        coEvery { bookingRepository.findBySpaceId(UUID.fromString(booking.spaceId)) } throws BookingException("The booking with spaceId ${booking.uuid} does not exist")

        val exception = assertFailsWith(BookingException::class) {
            bookingService.findBySpaceId(booking.spaceId)
        }
        assertEquals("The booking with spaceId ${booking.uuid} does not exist", exception.message)
    }

    @Test
    fun findByUserId() = runTest {
        coEvery { bookingRepository.findByUserId(UUID.fromString(booking.userId)) } returns listOf(booking)

        val result = bookingService.findByUserId(booking.userId)

        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(booking, result[0]) },
            { assertEquals(booking.id, result[0].id) },
            { assertEquals(booking.uuid, result[0].uuid) },
            { assertEquals(booking.userId, result[0].userId) },
            { assertEquals(booking.spaceId, result[0].spaceId) },
            { assertEquals(booking.status, result[0].status) },
            { assertEquals(booking.startTime, result[0].startTime) },
            { assertEquals(booking.endTime, result[0].endTime) },
            { assertEquals(booking.phone, result[0].phone) })
    }

    @Test
    fun notFoundByUserId() = runTest {
        coEvery { bookingRepository.findByUserId(UUID.fromString(booking.userId)) } throws BookingException("The booking with uuid ${booking.uuid} does not exist")

        val exception = assertFailsWith(BookingException::class) {
            bookingService.findByUserId(booking.userId)
        }
        assertEquals("The booking with uuid ${booking.uuid} does not exist", exception.message)
    }

    @Test
    fun save() = runTest {
        coEvery { bookingRepository.save(any()) } returns booking

        val result = bookingService.save(booking)

        assertAll(
            { assertEquals(booking, result) },
            { assertEquals(booking.id, result.id) },
            { assertEquals(booking.uuid, result.uuid) },
            { assertEquals(booking.userId, result.userId) },
            { assertEquals(booking.spaceId, result.spaceId) },
            { assertEquals(booking.status, result.status) },
            { assertEquals(booking.startTime, result.startTime) },
            { assertEquals(booking.endTime, result.endTime) },
            { assertEquals(booking.phone, result.phone) })
    }

    @Test
    fun failSave() = runTest {
        coEvery { bookingRepository.save(any()) } throws BookingException("Failed to save booking")

        val exception = assertFailsWith(BookingException::class) {
            bookingService.save(booking)
        }
        assertEquals("Failed to save booking", exception.message)
    }
    @Test
    fun update() = runTest {
        coEvery { bookingRepository.findAll() } returns listOf(booking)
        coEvery { bookingRepository.update(booking) } returns booking

        val result = bookingService.update(booking, booking.uuid)

        assertAll(
            { assertEquals(booking, result) },
            { assertEquals(booking.id, result.id) },
            { assertEquals(booking.uuid, result.uuid) },
            { assertEquals(booking.userId, result.userId) },
            { assertEquals(booking.spaceId, result.spaceId) },
            { assertEquals(booking.status, result.status) },
            { assertEquals(booking.startTime, result.startTime) },
            { assertEquals(booking.endTime, result.endTime) },
            { assertEquals(booking.phone, result.phone) })
    }

    @Test
    fun failUpdate() = runTest {
        coEvery { bookingRepository.findAll() } returns listOf(booking)
        coEvery { bookingRepository.update(booking) } throws BookingException("Failed to update booking with uuid ${booking.uuid}")

        val exception = assertFailsWith(BookingException::class) {
            bookingService.update(booking, booking.uuid)
        }
        assertEquals("Failed to update booking with uuid ${booking.uuid}", exception.message)
    }

    @Test
    fun delete() = runTest {
        coEvery { bookingRepository.delete(any()) } returns true

        val result = bookingService.delete(booking.uuid)

        assertTrue(result)
    }

    @Test
    fun failDelete() = runTest {
        coEvery { bookingRepository.delete(any()) } returns false

        val result = bookingService.delete(booking.uuid)

        assertFalse(result)
    }
}