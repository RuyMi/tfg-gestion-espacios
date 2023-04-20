package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.models.Booking
import org.litote.kmongo.Id
import org.litote.kmongo.eq

class BookingRepositoryImpl: BookingRepository{
    private val db = MongoDbManager.database
    override suspend fun findByUserId(id: String): List<Booking> {
         return db.getCollection<Booking>().find(Booking::userId eq id).toList()
    }

    override suspend fun findBySpaceId(id: String): List<Booking> {
        return db.getCollection<Booking>().find(Booking::spaceId eq id).toList()
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return db.getCollection<Booking>().find(Booking::status eq status).toList()
    }

    override suspend fun findAll(): List<Booking> {
        return db.getCollection<Booking>().find().toList()
    }

    override suspend fun findById(id: Id<Booking>): Booking? {
           return db.getCollection<Booking>().findOneById(id)
    }

    override suspend fun save(entity: Booking): Booking? {
        db.getCollection<Booking>().save(entity)?.let {
            return entity
        }
        return null
    }

    override suspend fun update(entity: Booking): Booking? {
        db.getCollection<Booking>().save(entity)?.let {
            return entity
        }
        return null
    }

    override suspend fun delete(entity: Booking): Boolean {
        return db.getCollection<Booking>().deleteOneById(entity.id).wasAcknowledged()
    }
}