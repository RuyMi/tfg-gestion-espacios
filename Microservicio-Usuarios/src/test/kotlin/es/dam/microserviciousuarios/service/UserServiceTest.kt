package es.dam.microserviciousuarios.service

import es.dam.microserviciousuarios.exceptions.UserBadRequestException
import es.dam.microserviciousuarios.exceptions.UserNotFoundException
import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.repositories.UsersRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    lateinit var usersRepository : UsersRepository

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    lateinit var usersService : UserService

    val user = User(
        id = ObjectId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c"),
        name = "test",
        username = "test",
        email = "test@test.com",
        password = "test1234",
        avatar = null
    )

    val userWrong = User(
            id = ObjectId(),
            uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c"),
            name = "",
            username = "",
            email = "",
            password = "",
            avatar = null
    )

     val uuidWrong = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d1")

    @Test
    fun loadUserByUsername() = runTest {
        coEvery { usersRepository.findUserByUsername(user.username) } returns listOf(user)

        val result = usersService.loadUserByUsername(user.username)
        assertAll(
            { assertEquals(user, result) },
            { assertEquals(user.username, result.username) },
            { assertEquals(user.password, result.password) }
        )
    }

    @Test
    fun loadUserByUsernameFailed() = runTest {
        coEvery { usersRepository.findUserByUsername(user.username) } returns listOf()

        val exception = assertThrows<UserNotFoundException> {
            usersService.loadUserByUsername(user.username)
        }
        assertEquals("User not found with username: ${user.username}", exception.message)
    }

    @Test
    fun findAll() = runTest {
        coEvery { usersRepository.findAll() } returns listOf(user)

        val result = usersService.findAll()
        assertAll(
            { assertEquals(1, result.size) },
            { assertEquals(user, result[0]) },
            { assertEquals(user.id, result[0].id) },
            { assertEquals(user.uuid, result[0].uuid) },
            { assertEquals(user.name, result[0].name) },
            { assertEquals(user.username, result[0].username) },
            { assertEquals(user.email, result[0].email) },
            { assertEquals(user.password, result[0].password) },
            { assertEquals(user.avatar, result[0].avatar) },
            { assertEquals(user.userRole, result[0].userRole) },
            { assertEquals(user.lastPasswordChangeAt, result[0].lastPasswordChangeAt) },
            { assertEquals(user.updatedAt, result[0].updatedAt) },
            { assertEquals(user.createdAt, result[0].createdAt) },
        )

    }

    @Test
    fun findUserById() = runTest {
        coEvery { usersRepository.findById(user.id!!) } returns Optional.of(user)

        val result = usersService.findUserById(user.id.toString())
        assertAll(
            { assertEquals(user, result) },
            { assertEquals(user.id, result.id) },
            { assertEquals(user.uuid, result.uuid) },
            { assertEquals(user.name, result.name) },
            { assertEquals(user.username, result.username) },
            { assertEquals(user.email, result.email) },
            { assertEquals(user.password, result.password) },
            { assertEquals(user.avatar, result.avatar) },
            { assertEquals(user.userRole, result.userRole) },
            { assertEquals(user.lastPasswordChangeAt, result.lastPasswordChangeAt) },
            { assertEquals(user.updatedAt, result.updatedAt) },
            { assertEquals(user.createdAt, result.createdAt) },
        )
    }

    @Test
    fun findUserByIdFailed() = runTest {
        coEvery { usersRepository.findById(user.id!!) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            usersService.findUserById(user.id.toString())
        }
        assertEquals("User with id ${user.id} not found.", exception.message)
    }

    @Test
    fun findByUuid() = runTest {
        coEvery { usersRepository.findUserByUuid(user.uuid) } returns listOf(user)

        val result = usersService.findByUuid(user.uuid.toString())
        assertAll(
            { assertEquals(user, result) },
            { assertEquals(user.id, result.id) },
            { assertEquals(user.uuid, result.uuid) },
            { assertEquals(user.name, result.name) },
            { assertEquals(user.username, result.username) },
            { assertEquals(user.email, result.email) },
            { assertEquals(user.password, result.password) },
            { assertEquals(user.avatar, result.avatar) },
            { assertEquals(user.userRole, result.userRole) },
            { assertEquals(user.lastPasswordChangeAt, result.lastPasswordChangeAt) },
            { assertEquals(user.updatedAt, result.updatedAt) },
            { assertEquals(user.createdAt, result.createdAt) },
        )
    }

    @Test
    fun findByUuidFailed404() = runTest {
        coEvery { usersRepository.findUserByUuid(user.uuid) } returns listOf()

        val exception = assertThrows<UserNotFoundException> {
            usersService.findByUuid(user.uuid.toString())
        }
        assertEquals("User with uuid ${user.uuid} not found.", exception.message)
    }

    @Test
    fun findByUuidFailed400() = runTest {
        coEvery { usersRepository.findUserByUuid(uuidWrong) } throws UserBadRequestException("Invalid UUID string: $uuidWrong")


        val exception = assertThrows<UserBadRequestException> {
            usersService.findByUuid(uuidWrong.toString())
        }
        assertEquals("Invalid UUID string: $uuidWrong", exception.message)
    }


    @Test
    fun save() = runTest {
        coEvery { usersRepository.findUserByUsername(user.username) } returns listOf()
        coEvery { usersRepository.findUserByEmail(user.email) } returns listOf()
        coEvery { passwordEncoder.encode(user.password) } returns user.password
        coEvery { usersRepository.save(any()) } returns user

        val result = usersService.save(user)
        assertAll(
            { assertEquals(user, result) },
            { assertEquals(user.id, result.id) },
            { assertEquals(user.uuid, result.uuid) },
            { assertEquals(user.name, result.name) },
            { assertEquals(user.username, result.username) },
            { assertEquals(user.email, result.email) },
            { assertEquals(user.password, result.password) },
            { assertEquals(user.avatar, result.avatar) },
            { assertEquals(user.userRole, result.userRole) },
            { assertEquals(user.lastPasswordChangeAt, result.lastPasswordChangeAt) },
            { assertEquals(user.updatedAt, result.updatedAt) },
            { assertEquals(user.createdAt, result.createdAt) },
        )
    }

    @Test
    fun saveFailedUsername() = runTest {
        coEvery { usersRepository.findUserByUsername(user.username) } returns listOf(user)
        coEvery { usersRepository.findUserByEmail(user.email) } returns listOf()
        coEvery { passwordEncoder.encode(user.password) } returns user.password
        coEvery { usersRepository.save(user) } returns user

        val exception = assertThrows<UserBadRequestException> {
            usersService.save(user)
        }
        assertEquals("Username already exists.", exception.message)
    }

    @Test
    fun saveFailedEmail() = runTest {
        coEvery { usersRepository.findUserByUsername(user.username) } returns listOf()
        coEvery { usersRepository.findUserByEmail(user.email) } returns listOf(user)
        coEvery { passwordEncoder.encode(user.password) } returns user.password
        coEvery { usersRepository.save(user) } returns user

        val exception = assertThrows<UserBadRequestException> {
            usersService.save(user)
        }
        assertEquals("Email already exists.", exception.message)
    }

    @Test
    fun saveFailed() = runTest {
        coEvery { usersRepository.findUserByUsername(userWrong.username) } returns listOf()
        coEvery { usersRepository.findUserByEmail(userWrong.email) } returns listOf()
        coEvery { passwordEncoder.encode(userWrong.password) } returns userWrong.password
        coEvery { usersRepository.save(userWrong) } returns userWrong

        val exception = assertThrows<UserBadRequestException> {
            usersService.save(userWrong)
        }

        assertEquals("Error creating the user. ->${exception.message!!.split(">")[1]}", exception.message)

    }

    @Test
    fun update() = runTest  {
        coEvery { usersRepository.save(any()) } returns user

        val result = usersService.update(user)
        assertAll(
            { assertEquals(user, result) },
            { assertEquals(user.id, result!!.id) },
            { assertEquals(user.uuid, result!!.uuid) },
            { assertEquals(user.name, result!!.name) },
            { assertEquals(user.username, result!!.username) },
            { assertEquals(user.email, result!!.email) },
            { assertEquals(user.password, result!!.password) },
            { assertEquals(user.avatar, result!!.avatar) },
            { assertEquals(user.userRole, result!!.userRole) },
            { assertEquals(user.lastPasswordChangeAt, result!!.lastPasswordChangeAt) },
            { assertEquals(user.updatedAt, result!!.updatedAt) },
            { assertEquals(user.createdAt, result!! .createdAt) },
        )
    }

    @Test
    fun updateFailed() = runTest  {
        coEvery { usersRepository.save(any()) } throws UserBadRequestException("Error updating the user.")

        val exception = assertThrows<UserBadRequestException> {
            usersService.update(user)
        }
        assertEquals("Error updating the user.", exception.message)
    }

    @Test
    fun deleteByUuid() = runTest {
        coEvery { usersRepository.findUserByUuid(user.uuid) } returns listOf(user)
        coEvery { usersRepository.deleteByUuid(user.uuid) } returns true
        assertTrue(usersService.deleteByUuid(user.uuid.toString()))
    }

    @Test
    fun deleteByUuidFailed404() = runTest {
        coEvery { usersRepository.findUserByUuid(user.uuid) } returns listOf()
        coEvery { usersRepository.deleteByUuid(user.uuid) } returns false

        val exception = assertThrows<UserNotFoundException> {
            usersService.deleteByUuid(user.uuid.toString())
        }
        assertEquals("User with uuid ${user.uuid} not found.", exception.message)
    }

    @Test
    fun deleteByUuidFailed400() = runTest {
        coEvery { usersRepository.findUserByUuid(uuidWrong) } throws UserBadRequestException("Invalid UUID string: $uuidWrong")
        coEvery { usersRepository.deleteByUuid(uuidWrong) } throws UserBadRequestException("Invalid UUID string: $uuidWrong")

        val exception = assertThrows<UserBadRequestException> {
            usersService.deleteByUuid(uuidWrong.toString())
        }
        assertEquals("Invalid UUID string: $uuidWrong", exception.message)

    }
}