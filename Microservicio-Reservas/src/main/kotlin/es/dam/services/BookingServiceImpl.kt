package es.dam.services

import es.dam.models.Booking
import es.dam.repositories.BookingCachedRepository
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.util.*

/**
 * Servicio de reservas.
 * @param repository: Repositorio cacheado de reservas.
 *
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 * @autor Mireya Sánchez Pinzón
 */
@Single
class BookingServiceImpl(
    @InjectedParam
    private val repository: BookingCachedRepository
) : BookingService {

    /**
     * Devuelve todas las reservas.
     * @return Lista de reservas.
     */
    override suspend fun findAll(): List<Booking> {
        return repository.findAll()
    }

    /**
     * Devuelve todas las reservas con un estado concreto.
     * @param status Estado de la reserva.
     * @return Lista de reservas.
     */
    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repository.findAllStatus(status)
    }

    /**
     * Devuelve todas las reservas asociadas a un espacio y a una fecha.
     * @param uuid Identificador del espacio.
     * @param date Fecha de la reserva.
     * @return Lista de reservas.
     */
    override suspend fun findByDate(uuid: String, date: String): List<Booking> {
        return repository.findByDate(UUID.fromString(uuid), LocalDate.parse(date))
    }

    /**
     * Devuelve una reserva por su identificador.
     * @param id Identificador de la reserva.
     * @return Reserva.
     * @throws BookingException si no existe la reserva.
     */
    override suspend fun findById(id: String): Booking {
        return repository.findById(UUID.fromString(id))
    }

    /**
     * Devuelve todas las reservas asociadas a un espacio.
     * @param id Identificador del espacio.
     * @return Lista de reservas.
     */
    override suspend fun findBySpaceId(id: String): List<Booking> {
        return repository.findBySpaceId(UUID.fromString(id))
    }

    /**
     * Devuelve todas las reservas asociadas a un usuario.
     * @param id Identificador del usuario.
     * @return Lista de reservas.
     */
    override suspend fun findByUserId(id: String): List<Booking> {
        return repository.findByUserId(UUID.fromString(id))
    }

    /**
     * Guarda una reserva.
     * @param entity Reserva a guardar.
     * @return Reserva guardada.
     * @throws BookingException si no se puede guardar la reserva.
     */
    override suspend fun save(entity: Booking): Booking {
        return repository.save(entity)
    }

    /**
     * Actualiza una reserva.
     * @param entity Reserva a actualizar.
     * @return Reserva actualizada.
     * @throws BookingException si no se puede actualizar la reserva.
     */
    override suspend fun update(entity: Booking, id: String): Booking {
        val bookingOriginal = repository.findById(UUID.fromString(id))
        val bookingUpdated = bookingOriginal.copy(
            userId = entity.userId,
            userName = entity.userName,
            spaceId = entity.spaceId,
            spaceName = entity.spaceName,
            image = entity.image,
            startTime = entity.startTime,
            endTime = entity.endTime,
            observations = entity.observations,
            status = entity.status
        )
        print(entity)
        print(bookingUpdated)
        return repository.update(bookingUpdated)
    }

    /**
     * Elimina una reserva.
     * @param id Identificador de la reserva.
     * @return true si se ha eliminado, false si no.
     * @throws BookingException si no se puede encontrar la reserva.
     */
    override suspend fun delete(id: String): Boolean {
        repository.findById(UUID.fromString(id))
        return repository.delete(UUID.fromString(id))
    }
}