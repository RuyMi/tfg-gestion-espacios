package es.dam.repositories

import es.dam.models.Space
import org.litote.kmongo.Id
import java.util.UUID

interface SpaceRepository: CrudRepository<Space, UUID>{
    suspend fun findByName(name: String): Space
    suspend fun findAllReservables(isReservable: Boolean): List<Space>
}