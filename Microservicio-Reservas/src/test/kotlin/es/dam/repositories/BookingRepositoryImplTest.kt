package es.dam.repositories

import es.dam.models.Booking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
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
        observations =  "test",
        userName = "test",
        spaceName = "test"
    )


    @BeforeEach
    fun setUp() = runTest {
        repository.save(booking)
    }

    @AfterEach
    fun tearDown() = runTest {
        repository.deleteAll()
    }

    @Test
    fun findByUserId() = runTest {
        val bookings = repository.findByUserId(UUID.fromString(booking.userId))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)

        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.observations, bookings[0].observations) },
            { assertEquals(booking.spaceName, bookings[0].spaceName) },
            { assertEquals(booking.userName, bookings[0].userName) },
        )
    }

    @Test
    fun findBySpaceId() = runTest {
        val bookings = repository.findBySpaceId(UUID.fromString(booking.spaceId))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.observations, bookings[0].observations) },
            { assertEquals(booking.spaceName, bookings[0].spaceName) },
            { assertEquals(booking.userName, bookings[0].userName) },
            )
    }

    @Test
    fun findAllStatus() = runTest {
        val bookings = repository.findAllStatus(booking.status)
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.observations, bookings[0].observations) },
            { assertEquals(booking.spaceName, bookings[0].spaceName) },
            { assertEquals(booking.userName, bookings[0].userName) },
            )
    }

    @Test
    fun findAll() = runTest {
        val bookings = repository.findAll()
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(1, bookings.size) },
            { assertEquals(booking.id, bookings[0].id) },
            { assertEquals(booking.uuid, bookings[0].uuid) },
            { assertEquals(booking.userId, bookings[0].userId) },
            { assertEquals(booking.spaceId, bookings[0].spaceId) },
            { assertEquals(booking.status, bookings[0].status) },
            { assertEquals(startTimeReduced, bookings[0].startTime.toString()) },
            { assertEquals(endTimeReduced, bookings[0].endTime.toString()) },
            { assertEquals(booking.observations, bookings[0].observations) },
            { assertEquals(booking.spaceName, bookings[0].spaceName) },
            { assertEquals(booking.userName, bookings[0].userName) },
            )
    }

    @Test
    fun findById() = runTest {
        val bookingTest = repository.findById(UUID.fromString(booking.uuid))
        val startTimeReduced = booking.startTime.toString().substring(0, 23)
        val endTimeReduced = booking.endTime.toString().substring(0, 23)

        assertAll(
            { assertEquals(booking.id, bookingTest.id) },
            { assertEquals(booking.uuid, bookingTest.uuid) },
            { assertEquals(booking.userId, bookingTest.userId) },
            { assertEquals(booking.spaceId, bookingTest.spaceId) },
            { assertEquals(booking.status, bookingTest.status) },
            { assertEquals(startTimeReduced, bookingTest.startTime.toString()) },
            { assertEquals(endTimeReduced, bookingTest.endTime.toString()) },
            { assertEquals(booking.observations, bookingTest.observations) },
            { assertEquals(booking.spaceName, bookingTest.spaceName) },
            { assertEquals(booking.userName, bookingTest.userName) },
            )
    }

    @Test
    fun save() = runTest {
        repository.delete(UUID.fromString(booking.uuid))
        val bookingTest = repository.save(booking)
        assertAll(
            { assertEquals(booking.id, bookingTest.id) },
            { assertEquals(booking.uuid, bookingTest.uuid) },
            { assertEquals(booking.userId, bookingTest.userId) },
            { assertEquals(booking.spaceId, bookingTest.spaceId) },
            { assertEquals(booking.status, bookingTest.status) },
            { assertEquals(booking.startTime, bookingTest.startTime) },
            { assertEquals(booking.endTime, bookingTest.endTime) },
            { assertEquals(booking.observations, bookingTest.observations) },
            { assertEquals(booking.spaceName, bookingTest.spaceName) },
            { assertEquals(booking.userName, bookingTest.userName) },
            )
    }

    @Test
    fun update() = runTest {
        val bookingTest = booking.copy(
            status = Booking.Status.APPROVED,
            startTime = LocalDateTime.now().plusHours(1),
            endTime = LocalDateTime.now().plusHours(2),
            observations = "updated"
        )
        val updatedBooking = repository.update(bookingTest)

        assertAll(
            { assertEquals(updatedBooking.id, bookingTest.id) },
            { assertEquals(updatedBooking.uuid, bookingTest.uuid) },
            { assertEquals(updatedBooking.userId, bookingTest.userId) },
            { assertEquals(updatedBooking.spaceId, bookingTest.spaceId) },
            { assertEquals(updatedBooking.status, bookingTest.status) },
            { assertEquals(updatedBooking.startTime, bookingTest.startTime) },
            { assertEquals(updatedBooking.endTime, bookingTest.endTime) },
            { assertEquals(updatedBooking.observations, bookingTest.observations) },
            { assertEquals(updatedBooking.spaceName, bookingTest.spaceName) },
            { assertEquals(updatedBooking.userName, bookingTest.userName) },
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