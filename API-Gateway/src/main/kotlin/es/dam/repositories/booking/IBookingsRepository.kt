package es.dam.repositories.booking

import es.dam.dto.*

interface IBookingsRepository {
    suspend fun findAll(token: String): BookingDataDTO
    suspend fun findById(token: String, id: Long): BookingResponseDTO
    suspend fun findBySpace(token: String, id: Long): BookingDataDTO
    suspend fun findByUser(token: String, id: Long): BookingDataDTO
    suspend fun findByStatus(token: String, status: String): BookingDataDTO
    suspend fun create(token: String, entity: BookingCreateDTO): BookingResponseDTO
    suspend fun update(token: String, id: Long, entity: BookingUpdateDTO): BookingResponseDTO
    suspend fun delete(token: String, id: Long)
}