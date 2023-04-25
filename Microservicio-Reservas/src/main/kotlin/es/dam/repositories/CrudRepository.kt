package es.dam.repositories


interface CrudRepository<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: ID): T?
    suspend fun save(entity: T): T?
    suspend fun update(entity: T): T?
    suspend fun delete(id: ID): Boolean
}