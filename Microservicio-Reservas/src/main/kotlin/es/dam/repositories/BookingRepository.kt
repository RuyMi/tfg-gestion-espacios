package es.dam.repositories

import es.dam.models.Booking
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

interface BookingRepository: CrudRepository<Booking, Id<Booking>>{
    suspend fun findByUserId(id: String): List<Booking>?
    suspend fun findBySpaceId(id: String): List<Booking>?
    suspend fun findAllStatus(status: Booking.Status): List<Booking>?
}