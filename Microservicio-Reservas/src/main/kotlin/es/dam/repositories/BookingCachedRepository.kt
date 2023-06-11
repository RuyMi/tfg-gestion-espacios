package es.dam.repositories

import es.dam.exceptions.BookingException
import es.dam.models.Booking
import es.dam.services.cache.BookingCacheImpl
import kotlinx.coroutines.*
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.util.*

/**
    * Repositorio cacheado de reservas.
    * @param repository: Repositorio de reservas.
    * @param cache: Cache de reservas.
    *
    * @author Mireya Sánchez Pinzón
    * @author Alejandro Sánchez Monzón
    * @author Rubén García-Redondo Marín
 */
@Single
@Named("BookingCachedRepository")
class BookingCachedRepository(
    @Named("BookingRepositoryImpl")
    private val repository: BookingRepositoryImpl,
    @Named("BookingCacheImpl")
    private val cache: BookingCacheImpl
): BookingRepository{

    private var refreshJob: Job? = null

    init {
        if (cache.hasRefreshAllCacheJob)
            refreshCacheJob()
    }

    /**
     * Refresca la cache de reservas.
     */
    private fun refreshCacheJob() {
        if (refreshJob != null)
            refreshJob?.cancel()

        refreshJob = CoroutineScope(Dispatchers.IO).launch {
            do {
                repository.findAll().forEach { Booking ->
                    cache.cache.put(UUID.fromString(Booking.uuid), Booking)
                }
                delay(cache.refreshTime)
            } while (true)
        }
    }

    /**
     * Devuelve todas las reservas.
     * @return Lista de reservas, vacía si no hay ninguna.
     */
    override suspend fun findAll(): List<Booking> {
        return if (!cache.hasRefreshAllCacheJob || cache.cache.asMap().isEmpty()) {
            repository.findAll()
        } else {
            cache.cache.asMap().values.toList()
        }
    }

    /**
     * Devuelve la reserva con el id indicado.
     * @param id: Id de la reserva.
     * @return Reserva con el id indicado.
     * @throws BookingException si no se encuentra la reserva.
     */
    override suspend fun findById(id: UUID): Booking {
        return cache.cache.get(id) ?: repository.findById(id)
            ?.also { cache.cache.put(id, it) }

        ?: throw BookingException("No se ha encontrado la reserva con uuid $id")
    }

    /**
     * Guarda la reserva indicada.
     * @param entity: Reserva a guardar.
     * @return Reserva guardada.
     * @throws BookingException si no se ha podido guardar la reserva.
     */
    override suspend fun save(entity: Booking): Booking {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            repository.save(entity)?: throw BookingException("Error al guardar la reserva con uuid ${entity.uuid}")
        }
        scope.launch {
            cache.cache.put(UUID.fromString(entity.uuid), entity)
        }
        return entity
    }

    /**
     * Actualiza la reserva indicada.
     * @param entity: Reserva a actualizar.
     * @return Reserva actualizada.
     * @throws BookingException si no se ha encontrado la reserva.
     */
    override suspend fun update(entity: Booking): Booking {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            repository.update(entity)?: throw BookingException("No se ha encontrado la reserva con uuid ${entity.uuid}")
        }
        scope.launch {
            cache.cache.put(UUID.fromString(entity.uuid), entity)
        }
        return entity
    }

    /**
     * Elimina la reserva con el id indicado.
     * @param id: Id de la reserva a eliminar.
     * @return true si se ha eliminado, false si no.
     * @throws BookingException si no se ha encontrado la reserva.
     */
    override suspend fun delete(id: UUID): Boolean {
        val scope = CoroutineScope(Dispatchers.IO)

        val delete = scope.async {
            repository.delete(id)
        }
        var result = false
        runCatching {
            result = delete.await()
        }.onFailure {
            throw BookingException("No se ha encontrado la reserva con uuid $id")
        }

        scope.launch {
            cache.cache.invalidate(id)
        }
        return result
    }

    /**
     * Devuelve todas las reservas de un usuario.
     * @param uuid: Id del usuario.
     * @return Lista de reservas del usuario, vacía si no tiene ninguna.
     */
    override suspend fun findByUserId(uuid: UUID): List<Booking> {
        return repository.findByUserId(uuid)
    }

    /**
     * Devuelve todas las reservas de un espacio.
     * @param uuid: Id del espacio.
     * @return Lista de reservas del espacio, vacía si no tiene ninguna.
     */
    override suspend fun findBySpaceId(uuid: UUID): List<Booking> {
        return repository.findBySpaceId(uuid)
    }

    /**
     * Devuelve todas las reservas con el estado indicado.
     * @param status: Estado de las reservas.
     * @return Lista de reservas con el estado indicado, vacía si no hay ninguna.
     */
    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repository.findAllStatus(status)
    }

    /**
     * Devuelve todas las reservas de un espacio y fecha indicadas.
     * @param uuid: Id del espacio.
     * @param date: Fecha de las reservas.
     * @return Lista de reservas del espacio y fecha indicadas, vacía si no hay ninguna.
     */
    override suspend fun findByDate(uuid: UUID, date: LocalDate): List<Booking> {
        return repository.findByDate(uuid, date)
    }

    /**
     * Elimina todas las reservas.
     * @return true si se han eliminado, false si no.
     */
    override suspend fun deleteAll(): Boolean {
        val scope = CoroutineScope(Dispatchers.IO)

        val delete = scope.async {
            repository.deleteAll()
        }
        var result = false
        runCatching {
            result = delete.await()
        }

        scope.launch {
            cache.cache.invalidateAll()
        }

        return result
    }
}