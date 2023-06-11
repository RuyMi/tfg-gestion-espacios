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

/**
 * Clase que implementa el repositorio de espacios. Se encarga de almacenar los espacios en la base de datos.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
@Named("SpaceRepositoryImpl")
class SpaceRepositoryImpl: SpaceRepository {
    private val db = MongoDbManager

    /**
     * Función que devuelve todos los espacios de la base de datos.
     *
     * @return List<Space>
     */
    override suspend fun findAll(): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find().toList()
    }

    /**
     * Función que devuelve el espacio con el nombre indicado.
     *
     * @param name Nombre del espacio a buscar.
     * @return Space
     */
    override suspend fun findByName(name: String): Space = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::name eq name).first()?: throw SpaceException("No se ha encontrado el espacio con nombre $name")
    }

    /**
     * Función que devuelve todos los espacios de la base de datos que sean reservables o no.
     *
     * @param isReservable Indica si se quieren buscar espacios reservables o no.
     * @return List<Space>
     */
    override suspend fun findAllReservables(isReservable: Boolean): List<Space> = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::isReservable eq isReservable).toList()
    }

    /**
     * Función que devuelve el espacio con el id indicado.
     *
     * @param id Id del espacio a buscar.
     * @return Space
     */
    override suspend fun findById(id: UUID): Space? = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().find(Space::uuid eq id.toString()).first()
    }

    /**
     * Función que guarda el espacio indicado en la base de datos.
     *
     * @param entity Espacio a guardar.
     * @return Space
     */
    override suspend fun save(entity: Space): Space? = withContext(Dispatchers.IO){
        db.database.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    /**
     * Función que actualiza el espacio indicado en la base de datos.
     *
     * @param entity Espacio a actualizar.
     * @return Space
     */
    override suspend fun update(entity: Space): Space? = withContext(Dispatchers.IO){
        db.database.getCollection<Space>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    /**
     * Función que elimina el espacio con el id indicado de la base de datos.
     *
     * @param id Id del espacio a eliminar.
     * @return Boolean
     */
    override suspend fun delete(id: UUID): Boolean = withContext(Dispatchers.IO){
        return@withContext db.database.getCollection<Space>().deleteMany(Space::uuid eq id.toString()).let {
            if(it.deletedCount >= 1L) return@let it.wasAcknowledged()
            throw SpaceException("No se ha encontrado el espacio con uuid $id")
        }
    }

    /**
     * Función que elimina todos los espacios de la base de datos.
     *
     * @return Boolean
     */
    override suspend fun deleteAll(): Boolean = withContext(Dispatchers.IO) {
        return@withContext db.database.getCollection<Space>().deleteMany().wasAcknowledged()
    }
}