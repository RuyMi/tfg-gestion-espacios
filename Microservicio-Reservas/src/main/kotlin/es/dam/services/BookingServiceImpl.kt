package es.dam.services

import es.dam.models.Booking
import es.dam.repositories.BookingRepositoryImpl
import org.litote.kmongo.Id

class BookingServiceImpl: BookingService{
//TODO Inyeccion de dependencias
    private val repo = BookingRepositoryImpl()
    override suspend fun findAll(): List<Booking> {
        return repo.findAll()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repo.findAllStatus(status)
    }

    override suspend fun findById(id: Id<Booking>): Booking? {
        return repo.findById(id)
    }

    override suspend fun findBySpaceId(id: String): List<Booking> {
        return repo.findBySpaceId(id)
    }

    override suspend fun findByUserId(id: String): List<Booking> {
        return repo.findByUserId(id)
    }

    override suspend fun save(booking: Booking): Booking? {
        return repo.save(booking)
    }

    override suspend fun update(booking: Booking): Booking? {
        return repo.update(booking)
    }

    override suspend fun delete(id: Id<Booking>): Boolean {
        return repo.delete(id)
    }

}