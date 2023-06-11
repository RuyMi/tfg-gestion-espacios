package es.dam.repositories

/**
 * Interfaz que define las operaciones CRUD básicas.
 *
 * @param T Tipo de la entidad
 * @param ID Tipo del identificador de la entidad
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface CrudRepository<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: ID): T?
    suspend fun save(entity: T): T?
    suspend fun update(entity: T): T?
    suspend fun delete(id: ID): Boolean
}