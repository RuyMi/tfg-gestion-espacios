package es.dam.repositories

import es.dam.models.Booking
import java.time.LocalDate
import java.util.*

/**
 * Interfaz del repositorio de reservas que extiende de CrudRepository.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface BookingRepository : CrudRepository<Booking, UUID> {
    suspend fun findByUserId(uuid: UUID): List<Booking>?
    suspend fun findBySpaceId(uuid: UUID): List<Booking>?
    suspend fun findAllStatus(status: Booking.Status): List<Booking>?
    suspend fun findByDate(uuid: UUID, date: LocalDate): List<Booking>
    suspend fun deleteAll(): Boolean
}