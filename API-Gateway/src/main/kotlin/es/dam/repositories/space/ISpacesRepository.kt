package es.dam.repositories.space

import es.dam.dto.*

interface ISpacesRepository {
    suspend fun findAll(token: String): SpaceDataDTO
    suspend fun findById(token: String, id: String): SpaceResponseDTO
    suspend fun findAllReservables(token: String, isReservable: Boolean): SpaceDataDTO
    suspend fun findByName(token: String, name: String): SpaceResponseDTO
    suspend fun create(token: String,  entity: SpaceCreateDTO): SpaceResponseDTO
    suspend fun update(token: String, id: String, entity: SpaceUpdateDTO): SpaceResponseDTO
    suspend fun delete(token: String, id: String)
}