package es.dam.repositories

import es.dam.exceptions.BookingException
import es.dam.models.Booking
import es.dam.services.cache.BookingCacheImpl
import kotlinx.coroutines.*
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.time.LocalDate
import java.util.*

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

    override suspend fun findAll(): List<Booking> {
        return if (!cache.hasRefreshAllCacheJob || cache.cache.asMap().isEmpty()) {
            repository.findAll()
        } else {
            cache.cache.asMap().values.toList()
        }
    }

    override suspend fun findById(id: UUID): Booking {
        return cache.cache.get(id) ?: repository.findById(id)
            ?.also { cache.cache.put(id, it) }

        ?: throw BookingException("No se ha encontrado la reserva con uuid $id")
    }

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

    override suspend fun findByUserId(uuid: UUID): List<Booking> {
        return repository.findByUserId(uuid)
    }

    override suspend fun findBySpaceId(uuid: UUID): List<Booking> {
        return repository.findBySpaceId(uuid)
    }

    override suspend fun findAllStatus(status: Booking.Status): List<Booking> {
        return repository.findAllStatus(status)
    }

    override suspend fun findByDate(uuid: UUID, date: LocalDate): List<Booking> {
        return repository.findByDate(uuid, date)
    }

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