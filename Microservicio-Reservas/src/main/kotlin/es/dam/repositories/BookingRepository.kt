package es.dam.repositories

import es.dam.models.Booking
import java.util.*

interface BookingRepository : CrudRepository<Booking, UUID> {
    suspend fun findByUserId(uuid: UUID): List<Booking>?
    suspend fun findBySpaceId(uuid: UUID): List<Booking>?
    suspend fun findAllStatus(status: Booking.Status): List<Booking>?
}