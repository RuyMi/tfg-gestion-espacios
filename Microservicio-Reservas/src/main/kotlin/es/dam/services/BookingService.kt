package es.dam.services

import es.dam.models.Booking
import org.litote.kmongo.Id

interface BookingService {
    suspend fun findAll(): List<Booking>
    suspend fun findAllStatus(status: Booking.Status): List<Booking>
    suspend fun findById(id: Id<Booking>): Booking
    suspend fun findBySpaceId(id: String): List<Booking>
    suspend fun findByUserId(id: String): List<Booking>
    suspend fun save(booking: Booking): Booking
    suspend fun update(booking: Booking): Booking
    suspend fun delete(id: Id<Booking>): Boolean
}