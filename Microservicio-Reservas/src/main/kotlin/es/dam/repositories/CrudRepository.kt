package es.dam.repositories

/**
 * Interfaz del CrudRepository.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface CrudRepository<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(uuid: ID): T?
    suspend fun save(entity: T): T?
    suspend fun update(entity: T): T?
    suspend fun delete(uuid: ID): Boolean
}