package es.dam.repositories

import es.dam.models.Space
import org.litote.kmongo.Id

interface SpaceRepository: CrudRepository<Space, Id<Space>>{
    suspend fun findByName(name: String): Space
    suspend fun findAllReservables(isReservable: Boolean): List<Space>
}