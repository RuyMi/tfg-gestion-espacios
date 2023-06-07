package es.dam.services.spaces

import es.dam.models.Space
import es.dam.repositories.SpaceCachedRepository
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single
import java.util.*

@Single
class SpaceServiceImpl(
    @InjectedParam
    private val repo: SpaceCachedRepository
): SpaceService {
    override suspend fun createSpace(space: Space): Space {
        return repo.save(space)
    }

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

    override suspend fun deleteSpace(spaceId: String): Boolean {
        return repo.delete(UUID.fromString(spaceId))
    }

    override suspend fun getSpaceById(id: String): Space {
        return repo.findById(UUID.fromString(id))
    }

    override suspend fun getAllSpaces(): List<Space> {
        return repo.findAll()
    }

    override suspend fun getSpaceByName(name: String): Space {
        return repo.findByName(name)
    }

    override suspend fun getAllSpacesReservables(isReservable: Boolean): List<Space> {
        return repo.findAllReservables(isReservable)
    }
}