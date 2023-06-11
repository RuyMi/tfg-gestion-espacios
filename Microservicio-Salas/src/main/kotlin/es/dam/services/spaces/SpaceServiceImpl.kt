package es.dam.services.spaces

import es.dam.models.Space
import es.dam.repositories.SpaceCachedRepository
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.util.*

/**
 * Clase que implementa el servicio de espacios. Implementa la interfaz [SpaceService]. Se encarga de gestionar los espacios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
class SpaceServiceImpl(
    @InjectedParam
    private val repo: SpaceCachedRepository
): SpaceService {
    /**
     * Función que guarda un espacio.
     *
     * @param space Espacio a guardar.
     * @return Space
     */
    override suspend fun createSpace(space: Space): Space {
        return repo.save(space)
    }

    /**
     * Función que actualiza un espacio.
     *
     * @param space Espacio a actualizar.
     * @param id Identificador del espacio a actualizar.
     * @return Space
     */
    override suspend fun updateSpace(space: Space, id: String): Space {
        val spaceOriginal = repo.findById(UUID.fromString(id))
        val spaceUpdated = spaceOriginal.copy(
            name = space.name,
            description = space.description,
            image = space.image,
            isReservable = space.isReservable,
            price = space.price,
            requiresAuthorization = space.requiresAuthorization,
            authorizedRoles = space.authorizedRoles,
            bookingWindow = space.bookingWindow
        )
        return repo.update(spaceUpdated)
    }

    /**
     * Función que elimina un espacio.
     *
     * @param spaceId Identificador del espacio a eliminar.
     * @return Boolean
     */
    override suspend fun deleteSpace(spaceId: String): Boolean {
        return repo.delete(UUID.fromString(spaceId))
    }

    /**
     * Función que devuelve un espacio.
     *
     * @param id Identificador del espacio a buscar.
     * @return Space
     */
    override suspend fun getSpaceById(id: String): Space {
        return repo.findById(UUID.fromString(id))
    }

    /**
     * Función que devuelve todos los espacios.
     *
     * @return List<Space>
     */
    override suspend fun getAllSpaces(): List<Space> {
        return repo.findAll()
    }

    /**
     * Función que devuelve el espacio con el nombre indicado.
     *
     * @param name Nombre del espacio a buscar.
     * @return Space
     */
    override suspend fun getSpaceByName(name: String): Space {
        return repo.findByName(name)
    }

    /**
     * Función que devuelve todos los espacios de la base de datos que sean reservables o no.
     *
     * @param isReservable Indica si se quieren buscar espacios reservables o no.
     * @return List<Space>
     */
    override suspend fun getAllSpacesReservables(isReservable: Boolean): List<Space> {
        return repo.findAllReservables(isReservable)
    }
}