package es.dam.repositories.booking

import es.dam.dto.BookingCreateDTO
import es.dam.dto.BookingDataDTO
import es.dam.dto.BookingResponseDTO
import es.dam.dto.BookingUpdateDTO

interface IBookingsRepository {
    suspend fun findAll(token: String): BookingDataDTO
    suspend fun findById(token: String, id: String): BookingResponseDTO
    suspend fun findBySpace(token: String, id: String): BookingDataDTO
    suspend fun findByUser(token: String, id: String): BookingDataDTO
    suspend fun findByStatus(token: String, status: String): BookingDataDTO
    suspend fun findByTime(token: String, id: String, date: String): BookingDataDTO
    suspend fun create(token: String, entity: BookingCreateDTO): BookingResponseDTO
    suspend fun update(token: String, id: String, entity: BookingUpdateDTO): BookingResponseDTO
    suspend fun delete(token: String, id: String)
}