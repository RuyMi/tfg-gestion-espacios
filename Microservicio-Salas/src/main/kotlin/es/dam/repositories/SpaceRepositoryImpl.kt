package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.exceptions.SpaceException
import es.dam.models.Space
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
@Named("SpaceRepositoryImpl")
class SpaceRepositoryImpl: SpaceRepository {
    private val db = MongoDbManager
    override suspend fun findAll(): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find().toList()
    }
    override suspend fun findByName(name: String): Space = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::name eq name).first()?: throw SpaceException("No se ha encontrado el espacio con nombre $name")
    }

    override suspend fun findAllReservables(isReservable: Boolean): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::isReservable eq isReservable).toList()
    }

    override suspend fun findById(id: UUID): Space? = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::uuid eq id.toString()).first()
    }

    override suspend fun save(entity: Space): Space? = withContext(Dispatchers.IO){
        db.database.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    override suspend fun update(entity: Space): Space? = withContext(Dispatchers.IO){
        db.database.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    override suspend fun delete(id: UUID): Boolean = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().deleteMany(Space::uuid eq id.toString()).let {
            if(it.deletedCount >= 1L) return@let it.wasAcknowledged()
            throw SpaceException("No se ha encontrado el espacio con uuid $id")
        }
    }

    override suspend fun deleteAll(): Boolean = withContext(Dispatchers.IO) {
        return@withContext db.database.getCollection<Space>().deleteMany().wasAcknowledged()
    }
}