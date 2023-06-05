package es.dam.repositories

import es.dam.models.Space
import java.util.*

interface SpaceRepository: CrudRepository<Space, UUID>{
    suspend fun findByName(name: String): Space
    suspend fun findAllReservables(isReservable: Boolean): List<Space>
    suspend fun deleteAll(): Boolean
}