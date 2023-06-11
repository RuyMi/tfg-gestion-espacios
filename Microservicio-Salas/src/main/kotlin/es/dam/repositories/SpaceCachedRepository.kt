package es.dam.repositories

import es.dam.exceptions.SpaceException
import es.dam.models.Space
import es.dam.services.cache.SpaceCacheImpl
import kotlinx.coroutines.*
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*

/**
 * Clase que implementa la caché de espacios. Se encarga de almacenar los espacios en caché y de actualizarlos cada cierto tiempo.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
@Named("SpaceCachedRepository")
class SpaceCachedRepository(
    @Named("SpaceRepositoryImpl")
    private val repository: SpaceRepositoryImpl,
    @Named("SpaceCacheImpl")
    private val cache: SpaceCacheImpl
): SpaceRepository{

    private var refreshJob: Job? = null

    /**
     * Inicializa la caché de espacios. Si la caché no tiene un trabajo de actualización de la caché, se crea uno.
     */
    init {
        if (cache.hasRefreshAllCacheJob)
            refreshCacheJob()
    }

    /**
     * Actualiza la caché de espacios.
     */
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

    /**
     * Función que devuelve todos los espacios de la caché. Si la caché no tiene un trabajo de actualización de la caché, busca los espacios en la base de datos.
     *
     * @return List<Space>
     */
    override suspend fun findAll(): List<Space> {
        return if (!cache.hasRefreshAllCacheJob || cache.cache.asMap().isEmpty()) {
            repository.findAll()
        } else {
            cache.cache.asMap().values.toList()
        }
    }

    /**
     * Función que devuelve un espacio por su id. Si la caché no tiene un trabajo de actualización de la caché, busca el espacio en la base de datos.
     *
     * @param id Id del espacio a buscar.
     * @return Space
     */
    override suspend fun findById(id: UUID): Space {
        return cache.cache.get(id) ?: repository.findById(id)
            ?.also { cache.cache.put(id, it) }

        ?: throw SpaceException("No se ha encontrado el espacio con uuid $id")
    }

    /**
     * Función que devuelve un espacio por su nombre
     *
     * @param name Nombre del espacio a buscar.
     * @return Space
     */
    override suspend fun findByName(name: String): Space {
        return repository.findByName(name)
    }

    /**
     * Función que devuelve todos los espacios reservables o no reservables
     *
     * @param isReservable Booleano que indica si se quieren espacios reservables o no reservables
     * @return List<Space>
     */
    override suspend fun findAllReservables(isReservable: Boolean): List<Space> {
        return repository.findAllReservables(isReservable)
    }

    /**
     * Funcion que guarda un espacio en la base de datos y en la caché
     *
     * @param entity Espacio a guardar
     * @return Space
     */
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

    /**
     * Funcion que actualiza un espacio en la base de datos y en la caché
     *
     * @param entity Espacio a actualizar
     * @return Space
     */
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

    /**
     * Funcion que elimina un espacio de la base de datos y de la caché
     *
     * @param id Id del espacio a eliminar
     * @return Boolean
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
            throw SpaceException("No se ha encontrado el espacio con uuid $id")
        }

        scope.launch {
            cache.cache.invalidate(id)
        }
        return result
    }

    /**
     * Funcion que elimina todos los espacios de la base de datos y de la caché
     *
     * @return Boolean
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