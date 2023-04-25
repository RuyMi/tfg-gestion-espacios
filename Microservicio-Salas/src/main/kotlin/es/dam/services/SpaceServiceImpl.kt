package es.dam.services

import es.dam.models.Space
import es.dam.repositories.SpaceRepositoryImpl
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import org.litote.kmongo.toId

@Single
class SpaceServiceImpl(
    @Named("SpaceRepositoryImpl")
    private val repo: SpaceRepositoryImpl
): SpaceService {
    override suspend fun createSpace(space: Space): Space {
        return repo.save(space)
    }

    override suspend fun updateSpace(space: Space): Space {
        return repo.update(space)
    }

    override suspend fun deleteSpace(spaceId: Id<Space>): Boolean {
        return repo.delete(spaceId)
    }

    override suspend fun getSpaceById(id: String): Space {
        return repo.findById(id.toId())
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