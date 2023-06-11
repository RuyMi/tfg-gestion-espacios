package es.dam.microserviciousuarios.service

import es.dam.microserviciousuarios.exceptions.UserBadRequestException
import es.dam.microserviciousuarios.exceptions.UserNotFoundException
import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 * Clase de servicio de usuarios. Se encarga de gestionar las operaciones de los usuarios.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Service
class UserService
@Autowired constructor(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    /**
     * Funcion que se encarga de cargar un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a cargar.
     * @return UserDetails
     */
    override fun loadUserByUsername(username: String?): UserDetails = runBlocking {
        return@runBlocking usersRepository.findUserByUsername(username!!).firstOrNull()
            ?: throw UserNotFoundException("User not found with username: $username")
    }

    /**
     * Funcion que devuelve todos los usuarios.
     *
     * @return List<User>
     */
    suspend fun findAll(): List<User> = withContext(Dispatchers.IO) {
        return@withContext usersRepository.findAll()
    }

    /**
     * Funcion que se encarga de poner 20 creditos a todos los usuarios.
     */
    fun poner20creditosAllUsers() {
        val users = usersRepository.findAll()
        users.forEach {
            val entity = it.copy(
                credits = 20,
                updatedAt = LocalDateTime.now()
            )
            usersRepository.save(entity)
        }
    }

    /**
     * Funcion que se encarga de buscar un usuario por su id.
     *
     * @param id Id del usuario a buscar.
     * @return User
     */
    suspend fun findUserById(id: String): User = withContext(Dispatchers.IO) {
        if (usersRepository.findById(ObjectId(id)).isPresent) {
            return@withContext usersRepository.findById(ObjectId(id)).get()
        } else {
            throw UserNotFoundException("User with id $id not found.")
        }
    }

    /**
     * Funcion que se encarga de buscar un usuario por su nombre de usuario y devolver si esta activo o no.
     *
     * @param username Nombre de usuario del usuario a buscar.
     * @return User
     */
    suspend fun isActive(username: String): Boolean = withContext(Dispatchers.IO) {
        if (usersRepository.findUserByUsername(username).isNotEmpty()) {
            return@withContext usersRepository.findUserByUsername(username).first().isActive
        } else {
            throw UserNotFoundException("User with username $username not found.")
        }
    }

    /**
     * Funcion que se encarga de buscar un usuario por su UUID.
     *
     * @param uuid UUID del usuario a buscar.
     * @return User
     */
    suspend fun findByUuid(uuid: String): User = withContext(Dispatchers.IO) {
        try {
            UUID.fromString(uuid)
        }catch(e: Exception){
            throw UserBadRequestException("Invalid UUID string: $uuid")
        }
        if (usersRepository.findUserByUuid(UUID.fromString(uuid)).isNotEmpty()) {
            return@withContext usersRepository.findUserByUuid(UUID.fromString(uuid)).first()
        } else {
            throw UserNotFoundException("User with uuid $uuid not found.")
        }
    }

    /**
     * Funcion que se encarga de guardar un usuario.
     *
     * @param user Usuario a guardar.
     * @return User
     */
    suspend fun save(user: User): User = withContext(Dispatchers.IO) {
        if (usersRepository.findUserByUsername(user.username)
                .firstOrNull() != null
        ) {
            throw UserBadRequestException("Username already exists.")
        }
        if (usersRepository.findUserByEmail(user.email)
                .firstOrNull() != null
        ) {
            throw UserBadRequestException("Email already exists.")
        }

        val saved = user.copy(
            id = ObjectId.get(),
            password = passwordEncoder.encode(user.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        try {
            return@withContext usersRepository.save(saved)
        } catch (e: Exception) {
            throw UserBadRequestException("Error creating the user. -> ${e.message}")
        }
    }

    /**
     * Funcion que se encarga de actualizar un usuario.
     *
     * @param user Usuario a actualizar.
     * @return User
     */
    suspend fun update(user: User): User? = withContext(Dispatchers.IO) {
        val saved = user.copy(
            updatedAt = LocalDateTime.now()
        )

        try {
            return@withContext usersRepository.save(saved)
        } catch (e: UserBadRequestException) {
            throw UserBadRequestException("Error updating the user.")
        }
    }

    /**
     * Funcion que se encarga de eliminar un usuario por su UUID.
     *
     * @param id UUID del usuario a eliminar.
     * @return Int
     */
    suspend fun deleteByUuid(uuid: String): Int = withContext(Dispatchers.IO) {
        try {
            UUID.fromString(uuid)
        }catch(e: Exception){
            throw UserBadRequestException("Invalid UUID string: $uuid")
        }
         if (usersRepository.findUserByUuid(UUID.fromString(uuid)).isNotEmpty()) {
             return@withContext usersRepository.deleteByUuid(UUID.fromString(uuid))
        } else {
            throw UserNotFoundException("User with uuid $uuid not found.")
        }
    }
}