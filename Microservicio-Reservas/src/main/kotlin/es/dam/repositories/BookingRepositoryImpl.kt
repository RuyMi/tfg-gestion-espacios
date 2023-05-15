package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.exceptions.BookingException
import es.dam.models.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class BookingRepositoryImpl : BookingRepository {
    private val manager = MongoDbManager
    override suspend fun findByUserId(uuid: UUID): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::userId eq uuid.toString()).toList().ifEmpty { throw BookingException("The booking with userId $uuid does not exist")  }
    }

    override suspend fun findBySpaceId(uuid: UUID): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::spaceId eq uuid.toString()).toList().ifEmpty { throw BookingException("The booking with spaceId $uuid does not exist") }
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::status eq status).toList().ifEmpty { throw BookingException("The booking with spaceId $uuid does not exist") }
    }

    override suspend fun findAll(): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find().toList()
    }

    override suspend fun findById(uuid: UUID): Booking = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::uuid eq uuid.toString()).first()
            ?: throw BookingException("The booking with uuid $uuid does not exist")
    }

    override suspend fun save(entity: Booking): Booking = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        throw BookingException("Failed to save booking")
    }

    override suspend fun update(entity: Booking): Booking = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        throw BookingException("Failed to update booking with uuid ${entity.uuid}")
    }

    override suspend fun delete(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().deleteOne(Booking::uuid eq uuid.toString()).let {
            if(it.deletedCount == 1L) return@let it.wasAcknowledged()
            throw BookingException("Failed to delete booking with uuid $uuid")
        }
    }

    override suspend fun deleteAll(): Boolean = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().deleteMany().wasAcknowledged()
    }
}