package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.exceptions.SpaceException
import es.dam.models.Space
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.Id
import org.litote.kmongo.eq

class SpaceRepositoryImpl: SpaceRepository {
    private val db = MongoDbManager.database

    override suspend fun findAll(): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.getCollection<Space>().find().toList()
    }
    override suspend fun findByName(name: String): Space = withContext(Dispatchers.IO){
        return@withContext db.getCollection<Space>().find(Space::name eq name).first()?: throw SpaceException("No se ha encontrado el espacio con nombre $name")
    }

    override suspend fun findAllReservables(isReservable: Boolean): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.getCollection<Space>().find(Space::isReservable eq isReservable).toList()
    }

    override suspend fun findById(id: Id<Space>): Space = withContext(Dispatchers.IO){
        return@withContext db.getCollection<Space>().findOneById(id)?: throw SpaceException("No se ha encontrado el espacio con id $id")
    }

    override suspend fun save(entity: Space): Space = withContext(Dispatchers.IO){
        db.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        throw SpaceException("Error al guardar el espacio con id ${entity.id}")
    }

    override suspend fun update(entity: Space): Space = withContext(Dispatchers.IO){
        db.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        throw SpaceException("No se ha encontrado el espacio con id ${entity.id}")
    }

    override suspend fun delete(id: Id<Space>): Boolean = withContext(Dispatchers.IO){
        return@withContext db.getCollection<Space>().deleteOneById(id).wasAcknowledged()
    }
}