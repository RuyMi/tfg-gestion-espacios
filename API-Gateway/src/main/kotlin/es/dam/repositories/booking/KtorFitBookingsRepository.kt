package es.dam.repositories.booking

import es.dam.dto.BookingDataDTO
import es.dam.dto.BookingResponseDTO
import es.dam.dto.BookingUpdateDTO
import es.dam.services.booking.KtorFitClientBookings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class KtorFitBookingsRepository: IBookingsRepository {

    private val client by lazy { KtorFitClientBookings.instance }

    override suspend fun findAll(token: String): BookingDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findAll() }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting bookings: ${e.message}")
        }
    }

    override suspend fun findById(token: String, id: Long): BookingResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.findById(id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting booking with id $id ${e.message}")
        }
    }

    override suspend fun findBySpace(token: String, id: Long): BookingDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findBySpace(id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting booking with space's id $id ${e.message}")
        }
    }

    override suspend fun findByUser(token: String, id: Long): BookingDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findByUser(id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting booking with users's id $id ${e.message}")
        }
    }

    override suspend fun findByStatus(token: String, status: String): BookingDataDTO = withContext(Dispatchers.IO) {
        val call = async { client.findByStatus(status) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error getting booking with status $status ${e.message}")
        }
    }

    override suspend fun create(token: String, id: Long, entity: BookingUpdateDTO): BookingResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.create(id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error creating booking: ${e.message}")
        }
    }

    override suspend fun update(token: String, id: Long, entity: BookingUpdateDTO): BookingResponseDTO = withContext(Dispatchers.IO) {
        val call = async { client.update(id, entity) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error updating booking with id $id: ${e.message}")
        }
    }

    override suspend fun delete(token: String, id: Long) = withContext(Dispatchers.IO) {
        val call = async { client.delete(id) }
        try {
            return@withContext call.await()
        } catch (e: Exception) {
            throw Exception("Error deleting booking with id $id: ${e.message}")
        }
    }
}