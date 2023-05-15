package es.dam.services

import es.dam.models.Booking
import java.util.*

interface BookingService {
    suspend fun findAll(): List<Booking>
    suspend fun findAllStatus(status: Booking.Status): List<Booking>
    suspend fun findById(id: String): Booking
    suspend fun findBySpaceId(id: String): List<Booking>
    suspend fun findByUserId(id: String): List<Booking>
    suspend fun save(entity: Booking): Booking
    suspend fun update(entity: Booking, id: String): Booking
    suspend fun delete(id: String): Boolean
}