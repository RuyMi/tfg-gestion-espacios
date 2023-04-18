package es.dam.microserviciousuarios.repositories

import es.dam.microserviciousuarios.models.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UsersRepository : MongoRepository<User, Long> {
    fun findUserByEmail(email: String): List<User>
    fun findUserByUuid(uuid: UUID): List<User>
    fun findUserByUsername(username: String): List<User>
}