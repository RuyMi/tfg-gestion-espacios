package es.dam.repositories

import es.dam.models.Booking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.litote.kmongo.id.toId
import java.time.LocalDateTime
import java.util.*

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
        val bookings = repository.findByUserId(UUID.fromString(booking.uuid))

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(booking.startTime, bookings[0].startTime) },
            { assertEquals(booking.endTime, bookings[0].endTime) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findBySpaceId() = runTest {
        val bookings = repository.findBySpaceId(UUID.fromString(booking.spaceId))

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(booking.startTime, bookings[0].startTime) },
            { assertEquals(booking.endTime, bookings[0].endTime) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findAllStatus() = runTest {
        val bookings = repository.findAllStatus(booking.status)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(booking.startTime, bookings[0].startTime) },
            { assertEquals(booking.endTime, bookings[0].endTime) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findAll() = runTest {
        val bookings = repository.findAll()

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking, bookings[0]) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(booking.startTime, bookings[0].startTime) },
            { assertEquals(booking.endTime, bookings[0].endTime) },
            { assertEquals(booking.phone, bookings[0].phone) }
        )
    }

    @Test
    fun findById() = runTest {
    }

    @Test
    fun save() = runTest {
    }

    @Test
    fun update() = runTest {
    }

    @Test
    fun delete() = runTest {
    }
}