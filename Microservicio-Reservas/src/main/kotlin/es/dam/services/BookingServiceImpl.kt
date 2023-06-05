package es.dam.services

import es.dam.models.Booking
import es.dam.repositories.BookingCachedRepository
import es.dam.repositories.BookingRepositoryImpl
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Single
class BookingServiceImpl(
    @InjectedParam
    private val repository: BookingCachedRepository
) : BookingService {
    override suspend fun findAll(): List<Booking> {
        return repository.findAll()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repository.findAllStatus(status)
    }

    override suspend fun findByDate(uuid: String, date: String): List<Booking> {
        return repository.findByDate(UUID.fromString(uuid), LocalDate.parse(date))
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
        val bookingOriginal = repository.findById(UUID.fromString(id))
        val bookingUpdated = bookingOriginal.copy(
            userId = entity.userId,
            userName = entity.userName,
            spaceId = entity.spaceId,
            spaceName = entity.spaceName,
            image = entity.image,
            startTime = entity.startTime,
            endTime = entity.endTime,
            observations = entity.observations,
            status = entity.status
        )
        print(entity)
        print(bookingUpdated)
        return repository.update(bookingUpdated)
    }

    override suspend fun delete(id: String): Boolean {
        repository.findById(UUID.fromString(id))
        return repository.delete(UUID.fromString(id))
    }
}