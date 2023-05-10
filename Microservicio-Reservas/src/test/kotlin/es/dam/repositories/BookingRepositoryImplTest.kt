package es.dam.repositories

import es.dam.models.Booking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.litote.kmongo.id.toId
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertContains

@OptIn(ExperimentalCoroutinesApi::class)
class BookingRepositoryImplTest {

   val repository = BookingRepositoryImpl()

    val booking = Booking(
        id = ObjectId().toId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c").toString(),
        userId = UUID.randomUUID().toString(),
        spaceId = UUID.randomUUID().toString(),
        status = Booking.Status.PENDING,
        startTime = LocalDateTime.now(),
        endTime = LocalDateTime.now(),
        phone = "123456789",
    )


    @BeforeEach
    fun setUp() = runTest {
        repository.save(booking)
    }

    @Test
    fun findByUserId() = runTest {
        val bookings = repository.findByUserId(UUID.fromString(booking.userId))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        println(startTimeReduced)
        println(bookings[0].startTime.toString())
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findBySpaceId() = runTest {
        val bookings = repository.findBySpaceId(UUID.fromString(booking.spaceId))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findAllStatus() = runTest {
        val bookings = repository.findAllStatus(booking.status)
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findAll() = runTest {
        val bookings = repository.findAll()
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findById() = runTest {
        val booking = repository.findById(UUID.fromString(booking.uuid))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(booking, booking) },
            { assertEquals(booking.id, booking.id) },
            { assertEquals(booking.uuid, booking.uuid) },
            { assertEquals(booking.userId, booking.userId) },
            { assertEquals(booking.spaceId, booking.spaceId) },
            { assertEquals(booking.status, booking.status) },
            { assertEquals(startTimeReduced, booking.startTime.toString()) },
            { assertEquals(endTimeReduced, booking.endTime.toString()) },
            { assertEquals(booking.phone, booking.phone) }
        )
    }

    @Test
    fun save() = runTest {
        repository.delete(UUID.fromString(booking.uuid))
        val booking = repository.save(booking)
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(booking, booking) },
            { assertEquals(booking.id, booking.id) },
            { assertEquals(booking.uuid, booking.uuid) },
            { assertEquals(booking.userId, booking.userId) },
            { assertEquals(booking.spaceId, booking.spaceId) },
            { assertEquals(booking.status, booking.status) },
            { assertEquals(startTimeReduced, booking.startTime.toString()) },
            { assertEquals(endTimeReduced, booking.endTime.toString()) },
            { assertEquals(booking.phone, booking.phone) }
        )
    }

    @Test
    fun update() = runTest {
        val booking = booking.copy(
            status = Booking.Status.APPROVED,
            startTime = LocalDateTime.now().plusHours(1),
            endTime = LocalDateTime.now().plusHours(2),
            phone = "987654321"
        )
        val updatedBooking = repository.update(booking)
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(updatedBooking, booking) },
            { assertEquals(updatedBooking.id, booking.id) },
            { assertEquals(updatedBooking.uuid, booking.uuid) },
            { assertEquals(updatedBooking.userId, booking.userId) },
            { assertEquals(updatedBooking.spaceId, booking.spaceId) },
            { assertEquals(updatedBooking.status, booking.status) },
            { assertEquals(updatedBooking.startTime.toString(), startTimeReduced) },
            { assertEquals(updatedBooking.endTime.toString(), endTimeReduced) },
            { assertEquals(updatedBooking.phone, booking.phone) }
        )
    }

    @Test
    fun delete() = runTest {
        val deleted = repository.delete(UUID.fromString(booking.uuid))
        assertTrue(deleted)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUpAll(): Unit = runTest {
            val repository = BookingRepositoryImpl()
            repository.deleteAll()
        }
    }
}