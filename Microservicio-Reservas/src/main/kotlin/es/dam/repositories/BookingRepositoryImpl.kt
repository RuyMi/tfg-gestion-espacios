package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.exceptions.BookingException
import es.dam.models.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.eq

@Single
@Named("BookingRepositoryImpl")
class BookingRepositoryImpl(
    private val db: MongoDbManager
): BookingRepository{
    override suspend fun findByUserId(id: String): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext db.database.getCollection<Booking>().find(Booking::userId eq id).toList()
    }

    override suspend fun findBySpaceId(id: String): List<Booking> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Booking>().find(Booking::spaceId eq id).toList()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Booking>().find(Booking::status eq status).toList()
    }

    override suspend fun findAll(): List<Booking> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Booking>().find().toList()
    }

    override suspend fun findById(id: Id<Booking>): Booking = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Booking>().findOneById(id)?: throw BookingException("No se ha encontrado la reserva con id $id")
    }

    override suspend fun save(entity: Booking): Booking = withContext(Dispatchers.IO){
        db.database.getCollection<Booking>().save(entity)?.let {
            return@let entity
        }
        throw BookingException("Error al guardar la reserva")
    }

    override suspend fun update(entity: Booking): Booking = withContext(Dispatchers.IO){
        db.database.getCollection<Booking>().save(entity)?.let {
            return@let entity
        }
        throw BookingException("Error al actualizar la reserva con id ${entity.id}")
    }

    override suspend fun delete(id: Id<Booking>): Boolean = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Booking>().deleteOneById(id).wasAcknowledged()
    }
}