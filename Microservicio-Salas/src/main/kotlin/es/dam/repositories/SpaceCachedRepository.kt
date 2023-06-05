package es.dam.repositories

import es.dam.exceptions.SpaceException
import es.dam.models.Space
import es.dam.services.cache.SpaceCacheImpl
import kotlinx.coroutines.*
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*

@Single
@Named("SpaceCachedRepository")
class SpaceCachedRepository(
    @Named("SpaceRepositoryImpl")
    private val repository: SpaceRepositoryImpl,
    @Named("SpaceCacheImpl")
    private val cache: SpaceCacheImpl
): SpaceRepository{

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
                repository.findAll().forEach { space ->
                    cache.cache.put(UUID.fromString(space.uuid), space)
                }
                delay(cache.refreshTime)
            } while (true)
        }
    }

    override suspend fun findAll(): List<Space> {
        return if (!cache.hasRefreshAllCacheJob || cache.cache.asMap().isEmpty()) {
            repository.findAll()
        } else {
            cache.cache.asMap().values.toList()
        }
    }

    override suspend fun findById(id: UUID): Space {
        return cache.cache.get(id) ?: repository.findById(id)
            ?.also { cache.cache.put(id, it) }

        ?: throw SpaceException("No se ha encontrado el espacio con uuid $id")
    }

    override suspend fun findByName(name: String): Space {
        return repository.findByName(name)
    }

    override suspend fun findAllReservables(isReservable: Boolean): List<Space> {
        return repository.findAllReservables(isReservable)
    }

    override suspend fun save(entity: Space): Space {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            repository.save(entity)?: throw SpaceException("Error al guardar el espacio con uuid ${entity.uuid}")
        }
        scope.launch {
            cache.cache.put(UUID.fromString(entity.uuid), entity)
        }
        return entity
    }

    override suspend fun update(entity: Space): Space {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            repository.update(entity)?: throw SpaceException("No se ha encontrado el espacio con uuid ${entity.uuid}")
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
            throw SpaceException("No se ha encontrado el espacio con uuid $id")
        }

        scope.launch {
            cache.cache.invalidate(id)
        }
        return result
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