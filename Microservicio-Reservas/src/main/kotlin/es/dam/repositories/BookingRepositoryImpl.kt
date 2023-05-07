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
        return@withContext manager.database.getCollection<Booking>().find(Booking::userId eq uuid.toString()).toList()
    }

    override suspend fun findBySpaceId(uuid: UUID): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::spaceId eq uuid.toString()).toList()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::status eq status).toList()
    }

    override suspend fun findAll(): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find().toList()
    }

    override suspend fun findById(uuid: UUID): Booking = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::uuid eq uuid.toString()).first()
            ?: throw BookingException("No se ha encontrado la reserva con uuid $uuid")
    }

    override suspend fun save(entity: Booking): Booking = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        throw BookingException("Error al guardar la reserva")
    }

    override suspend fun update(entity: Booking): Booking = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        throw BookingException("Error al actualizar la reserva con uuid ${entity.uuid}")
    }

    override suspend fun delete(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().deleteOne(Booking::uuid eq uuid.toString())
            .wasAcknowledged()
    }
}