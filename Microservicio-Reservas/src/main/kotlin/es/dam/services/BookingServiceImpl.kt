package es.dam.services

import es.dam.models.Booking
import es.dam.repositories.BookingRepositoryImpl
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.util.*

@Single
class BookingServiceImpl(
    @InjectedParam
    private val repository: BookingRepositoryImpl
) : BookingService {
    override suspend fun findAll(): List<Booking> {
        return repository.findAll()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repository.findAllStatus(status)
    }

    override suspend fun findById(id: String): Booking {
        return repository.findById(UUID.fromString(id))
    }

    override suspend fun findBySpaceId(id: String): List<Booking> {
        return repository.findBySpaceId(UUID.fromString(id))
    }

    override suspend fun findByUserId(id: String): List<Booking> {
        return repository.findByUserId(UUID.fromString(id))
    }

    override suspend fun save(entity: Booking): Booking {
        return repository.save(entity)
    }

    override suspend fun update(entity: Booking, id: String): Booking {
        val bookingOriginal = repository.findAll().first { it.id.toString() == id }
        val bookingUpdated = bookingOriginal.copy(
            userId = entity.userId,
            spaceId = entity.spaceId,
            startTime = entity.startTime,
            endTime = entity.startTime,
            phone = entity.phone,
            status = entity.status
        )
        return repository.update(bookingUpdated)
    }

    override suspend fun delete(id: String): Boolean {
        return repository.delete(UUID.fromString(id))
    }
}