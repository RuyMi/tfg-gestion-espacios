package es.dam.microserviciousuarios.repositories

import es.dam.microserviciousuarios.models.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

/**
 * Repositorio de usuarios. Se encarga de gestionar las operaciones CRUD de los usuarios. Extiende de MongoRepository.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface UsersRepository : MongoRepository<User, ObjectId> {
    fun findUserByEmail(email: String): List<User>
    fun findUserByUuid(uuid: UUID): List<User>
    fun findUserByUsername(username: String): List<User>
    fun deleteByUuid(uuid: UUID): Int
}