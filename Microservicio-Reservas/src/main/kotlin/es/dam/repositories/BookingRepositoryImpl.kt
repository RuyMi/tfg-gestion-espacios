package es.dam.repositories

import es.dam.db.MongoDbManager
import es.dam.models.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.time.LocalDate
import java.util.*

/**
 * Repositorio de reservas.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
@Named("BookingRepositoryImpl")
class BookingRepositoryImpl : BookingRepository {
    private val manager = MongoDbManager

    /**
     * Devuelve todas las reservas de un usuario.
     * @param uuid: Identificador del usuario.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findByUserId(uuid: UUID): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::userId eq uuid.toString()).toList().ifEmpty { emptyList() }
    }

    /**
     * Devuelve todas las reservas de un espacio.
     * @param uuid: Identificador del espacio.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findBySpaceId(uuid: UUID): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::spaceId eq uuid.toString()).toList().ifEmpty { emptyList() }
    }

    /**
     * Devuelve todas las reservas con el estado indicado.
     * @param status: Estado de la reserva.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findAllStatus(status: Booking.Status): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::status eq status).toList().ifEmpty { emptyList() }
    }

    /**
     * Devuelve todas las reservas asociadas a un espacio y a una fecha.
     * @param uuid: Identificador del espacio.
     * @param date: Fecha de la reserva.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findByDate(uuid: UUID, date: LocalDate): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::spaceId eq uuid.toString()).toList().filter { it.startTime.toString().split("T")[0] == date.toString() }
    }

    /**
     * Devuelve todas las reservas.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findAll(): List<Booking> = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find().toList()
    }

    /**
     * Devuelve una reserva por su identificador.
     * @param uuid: Identificador de la reserva.
     * @return Reserva, null si no existe.
     */
    override suspend fun findById(uuid: UUID): Booking? = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().find(Booking::uuid eq uuid.toString()).first()
    }

    /**
     * Guarda una reserva.
     * @param entity: Reserva a guardar.
     * @return Reserva guardada, null si no se ha podido guardar.
     */
    override suspend fun save(entity: Booking): Booking? = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    /**
     * Actualiza una reserva.
     * @param entity: Reserva a actualizar.
     * @return Reserva actualizada, null si no se ha podido actualizar.
     */
    override suspend fun update(entity: Booking): Booking? = withContext(Dispatchers.IO) {
        manager.database.getCollection<Booking>().save(entity)?.let {
            return@withContext entity
        }
        return@withContext null
    }

    /**
     * Elimina una reserva.
     * @param uuid: Identificador de la reserva.
     * @return true si se ha eliminado, false si no.
     */
    override suspend fun delete(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().deleteOne(Booking::uuid eq uuid.toString()).let {
            return@let it.wasAcknowledged()
        }
    }

    /**
     * Elimina todas las reservas.
     * @return true si se han eliminado, false si no.
     */
    override suspend fun deleteAll(): Boolean = withContext(Dispatchers.IO) {
        return@withContext manager.database.getCollection<Booking>().deleteMany().wasAcknowledged()
    }
}