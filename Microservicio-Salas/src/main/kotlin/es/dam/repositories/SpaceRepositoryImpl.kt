package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.exceptions.SpaceException
import es.dam.models.Space
import org.litote.kmongo.Id
import org.litote.kmongo.eq

class SpaceRepositoryImpl: SpaceRepository {
    private val db = MongoDbManager.database

    override suspend fun findAll(): List<Space> {
        return db.getCollection<Space>().find().toList()
    }
    override suspend fun findByName(name: String): Space {
        return db.getCollection<Space>().find(Space::name eq name).first()?: throw SpaceException("No se ha encontrado el espacio con nombre $name")
    }

    override suspend fun findAllReservables(isReservable: Boolean): List<Space> {
        return db.getCollection<Space>().find(Space::isReservable eq isReservable).toList()
    }

    override suspend fun findById(id: Id<Space>): Space {
        return db.getCollection<Space>().findOneById(id)?: throw SpaceException("No se ha encontrado el espacio con id $id")
    }

    override suspend fun save(entity: Space): Space {
        db.getCollection<Space>().save(entity)?.let {
            return entity
        }
        throw SpaceException("Error al guardar el espacio con id ${entity.id}")
    }

    override suspend fun update(entity: Space): Space {
        db.getCollection<Space>().save(entity)?.let {
            return entity
        }
        throw SpaceException("No se ha encontrado el espacio con id ${entity.id}")
    }

    override suspend fun delete(entity: Space): Boolean {
        return db.getCollection<Space>().deleteOneById(entity.id).wasAcknowledged()
    }
}