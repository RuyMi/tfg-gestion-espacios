package es.dam.microserviciousuarios.service

import es.dam.microserviciousuarios.exceptions.UserBadRequestException
import es.dam.microserviciousuarios.exceptions.UserNotFoundException
import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService
@Autowired constructor(
    private val usersRepository: UsersRepository,
    //private val passwordEncoder: PasswordEncoder
)
//: UserDetailsService
{

//    override fun loadUserByUsername(username: String?): UserDetails = runBlocking {
//        return@runBlocking usersRepository.findUserByUsername(username!!).firstOrNull()
//            ?: throw UserNotFoundException("User doesn't found with username: $username")
//    }

    suspend fun findAll() = withContext(Dispatchers.IO) {
        return@withContext usersRepository.findAll()
    }

    suspend fun findUserById(id: Long) = withContext(Dispatchers.IO) {
        if (usersRepository.findById(id).isPresent) {
            return@withContext usersRepository.findById(id).get()
        } else {
            throw UserNotFoundException("User with id $id not found.")
        }
    }

    suspend fun save(user: User, isAdmin: Boolean = false): User = withContext(Dispatchers.IO) {
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
//            password = passwordEncoder.encode(user.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        try {
            return@withContext usersRepository.save(saved)
        } catch (e: Exception) {
            throw UserBadRequestException("Error creating the user. -> ${e.message}")
        }
    }

    suspend fun update(user: User): User? = withContext(Dispatchers.IO) {
        try {
            return@withContext usersRepository.save(user)
        } catch (e: UserBadRequestException) {
            throw UserBadRequestException("Error updating the user.")
        }
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        if (usersRepository.findById(id).isPresent) {
            usersRepository.deleteById(id)
        } else {
            throw UserNotFoundException("User with id $id not found.")
        }
    }
}