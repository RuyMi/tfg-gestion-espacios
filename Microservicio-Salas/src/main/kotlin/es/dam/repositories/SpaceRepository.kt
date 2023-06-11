package es.dam.repositories

import es.dam.models.Space
import java.util.*

/**
 * Interfaz que define las operaciones adicionales a las CRUD básicas.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface SpaceRepository: CrudRepository<Space, UUID>{
    suspend fun findByName(name: String): Space
    suspend fun findAllReservables(isReservable: Boolean): List<Space>
    suspend fun deleteAll(): Boolean
}