package es.dam.repositories

import es.dam.models.Space

interface CrudRepository<T, ID> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: ID): Space?
    suspend fun save(entity: T): Space?
    suspend fun update(entity: T): Space?
    suspend fun delete(entity: T): Boolean
}