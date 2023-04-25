package es.dam.services

import es.dam.models.Space
import org.litote.kmongo.Id

interface SpaceService {
    suspend fun createSpace(space: Space): Space?
    suspend fun updateSpace(space: Space): Space?
    suspend fun deleteSpace(spaceId: Id<Space>): Boolean
    suspend fun getSpaceById(id: String): Space?
    suspend fun getAllSpaces(): List<Space>
    suspend fun getSpaceByName(name: String): Space?
    suspend fun getAllSpacesReservables(isReservable: Boolean): List<Space>
}