package es.dam.services.spaces

import es.dam.models.Space

interface SpaceService {
    suspend fun createSpace(space: Space): Space?
    suspend fun updateSpace(space: Space, id: String): Space?
    suspend fun deleteSpace(spaceId: String): Boolean
    suspend fun getSpaceById(id: String): Space?
    suspend fun getAllSpaces(): List<Space>
    suspend fun getSpaceByName(name: String): Space?
    suspend fun getAllSpacesReservables(isReservable: Boolean): List<Space>
}