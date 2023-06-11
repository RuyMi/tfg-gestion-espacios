package es.dam.microserviciousuarios.controllers

import es.dam.microserviciousuarios.dto.UserLoginDTO
import es.dam.microserviciousuarios.dto.UserRegisterDTO
import es.dam.microserviciousuarios.dto.UserUpdateDTO
import es.dam.microserviciousuarios.exceptions.UserBadRequestException
import es.dam.microserviciousuarios.exceptions.UserNotFoundException
import es.dam.microserviciousuarios.mappers.toDTO
import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.service.UserService
import es.dam.microserviciousuarios.utils.JWTUtils
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.server.ResponseStatusException
import java.util.*

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
@SpringBootTest
class UsersControllerTest {
    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var authenticationManager: AuthenticationManager

    @MockK
    private lateinit var authentication: Authentication

    @MockK
    private lateinit var jwtUtils: JWTUtils

    @InjectMockKs
    lateinit var usersController: UsersController

    val user = User(
        id = ObjectId(),
        uuid = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d166c"),
        name = "test",
        username = "test",
        email = "test@test.com",
        password = "test1234",
        avatar = null
    )

    val userUpdate = UserUpdateDTO(
            name= "updated",
            username = "updated",
            avatar = null,
            credits = 20,
            userRole = setOf("USER"),
            isActive = true
    )

    val userRegister = UserRegisterDTO(
            name= "updated",
            username = "updated",
            email = "updted@test.com",
            password = "updated1234",
            avatar = null,
            userRole = setOf("USER"),
            isActive = true
    )

    val uuidWrong = UUID.fromString("c060c959-8462-4a0f-9265-9af4f54d1")

    val userLogin = UserLoginDTO(
            username = "test",
            password = "test"
    )

    val userResponseDTO = user.toDTO()

    val userRegisterWrong = UserRegisterDTO(
            name= "",
            username = "",
            email = "",
            password = "",
            avatar = null,
            userRole = setOf(""),
            isActive = true
    )

    @Test
    fun login() = runTest {
        coEvery { authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password)) } returns authentication
        coEvery { authentication.principal } returns user
        coEvery{jwtUtils.generateToken(user)} returns "token"

        val result = usersController.login(userLogin)
        val responseDTO = result.body

        assertAll(
                { assertNotNull(result) },
                { assertNotNull(responseDTO) },
                { assertEquals(responseDTO!!.token, "token") },
                { assertEquals(userResponseDTO.id, responseDTO!!.user.id) },
                { assertEquals(userResponseDTO.username, responseDTO!!.user.username) },
                { assertEquals(userResponseDTO.email, responseDTO!!.user.email) },
                { assertEquals(result.statusCode, HttpStatus.OK) },
        )
    }


    @Test
    fun loginFailedUnauthorized() = runTest {
        coEvery { authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userLogin.username, userLogin.password)) } throws UserNotFoundException("User not found.")
        coEvery { authentication.principal } returns user
        coEvery{jwtUtils.generateToken(user)} returns "token"

        val exception = assertThrows<ResponseStatusException> {
            usersController.login(userLogin)
        }

        assertEquals("401 UNAUTHORIZED \"Usuario o contraseña incorrectos\"", exception.message)
    }


    @Test
    fun register() = runTest {
        coEvery { userService.save(any()) } returns user
        coEvery { jwtUtils.generateToken(any()) } returns "token"

        val result = usersController.register(userRegister)
        val responseDTO = result.body

        assertAll(
                { assertNotNull(result) },
                { assertNotNull(responseDTO) },
                { assertEquals(userResponseDTO.id, responseDTO!!.user.id) },
                { assertEquals(userResponseDTO.username, responseDTO!!.user.username) },
                { assertEquals(userResponseDTO.email, responseDTO!!.user.email) },
                { assertEquals(result.statusCode, HttpStatus.CREATED) },
        )
    }

    @Test
    fun registerEmailFailed() = runTest  {
        coEvery { userService.save(any()) } throws UserBadRequestException("Ya existe un usuario con ese nombre o correo")
        coEvery { jwtUtils.generateToken(any()) } returns "token"

        val exception = assertThrows<ResponseStatusException> {
            usersController.register(userRegister)
        }

        assertEquals("400 BAD_REQUEST \"Ya existe un usuario con ese nombre o correo\"", exception.message)
    }

    @Test
    fun registerUsernameFailed() = runTest  {
        coEvery { userService.save(any()) } throws UserBadRequestException("Ya existe un usuario con ese nombre o correo")
        coEvery { jwtUtils.generateToken(any()) } returns "token"

        val exception = assertThrows<ResponseStatusException> {
            usersController.register(userRegister)
        }

        assertEquals("400 BAD_REQUEST \"Ya existe un usuario con ese nombre o correo\"", exception.message)
    }

    @Test
    fun registerFailed400() = runTest  {
        coEvery { userService.save(any()) } throws UserBadRequestException("Error creating the user. ->")
        coEvery { jwtUtils.generateToken(any()) } returns "token"

        val exception = assertThrows<ResponseStatusException> {
            usersController.register(userRegisterWrong)
        }

        assertEquals("400 BAD_REQUEST \"Ya existe un usuario con ese nombre o correo\"", exception.message)
    }

    @Test
    fun findAll() = runTest {
        coEvery { userService.findAll() } returns listOf(user)

        val result = usersController.findAll(user)
        val dtoBody = result.body!!.data

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(dtoBody) },
            { assertEquals(1, dtoBody.size) },
            { assertEquals(userResponseDTO.id, dtoBody[0].id) },
            { assertEquals(userResponseDTO.username, dtoBody[0].username) },
            { assertEquals(userResponseDTO.email, dtoBody[0].email) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
        )
    }

    @Test
    fun findById() = runTest {
        coEvery { userService.findByUuid(any()) } returns user

        val result = usersController.findById(user.uuid.toString())
        val responseDTO = result.body!!

        assertAll(
            { assertNotNull(result) },
            { assertNotNull(responseDTO) },
            { assertEquals(userResponseDTO.id, responseDTO.id) },
            { assertEquals(userResponseDTO.uuid, responseDTO.uuid) },
            { assertEquals(userResponseDTO.username, responseDTO.username) },
            { assertEquals(userResponseDTO.email, responseDTO.email) },
            { assertEquals(result.statusCode, HttpStatus.OK) },
        )
    }

    @Test
    fun findByIdFailed404() = runTest  {
        coEvery { userService.findByUuid(any()) } throws UserNotFoundException("Usuario con uuid: ${user.uuid} no encontrado")

        val exception = assertThrows<ResponseStatusException> {
            usersController.findById(user.uuid.toString())
        }

        assertEquals("404 NOT_FOUND \"Usuario con uuid: ${user.uuid} no encontrado\"", exception.message)
    }

    @Test
    fun findByIdFailed400() = runTest  {
        coEvery { userService.findByUuid(uuidWrong.toString()) } throws UserBadRequestException("Invalid UUID string: $uuidWrong")

        val exception = assertThrows<ResponseStatusException> {
            usersController.findById(uuidWrong.toString())
        }

        assertEquals("400 BAD_REQUEST \"Invalid UUID string: $uuidWrong\"", exception.message)
    }

    @Test
    fun update() = runTest {
        coEvery { userService.update(user) } returns user

        val result = usersController.update(user, userUpdate)
        val responseDTO = result.body!!

        assertAll(
                { assertNotNull(result) },
                { assertNotNull(responseDTO) },
                { assertEquals(userResponseDTO.id, responseDTO.id) },
                { assertEquals(userResponseDTO.username, responseDTO.username) },
                { assertEquals(userResponseDTO.email, responseDTO.email) },
                { assertEquals(result.statusCode, HttpStatus.OK) },
        )
    }

    @Test
    fun updateFailed400() = runTest  {
        coEvery { userService.update(any()) } throws UserBadRequestException("Error updating the user.")

        val exception = assertThrows<ResponseStatusException> {
            usersController.update(user, userUpdate)
        }

        assertEquals("400 BAD_REQUEST \"Invalid UUID string: c060c959-8462-4a0f-9265-9af4f54d166c\"" , exception.message)
    }

    @Test
    fun updateFailed404() = runTest  {
        coEvery { userService.findByUuid(any()) } throws UserNotFoundException("User with uuid ${user.uuid} not found.")
        coEvery { userService.update(any()) } throws UserNotFoundException("Error updating the user.")

        val exception = assertThrows<ResponseStatusException> {
            usersController.update(user, userUpdate)
        }

        assertEquals("404 NOT_FOUND \"User not found with uuid: ${user.uuid}\"", exception.message)
    }

    @Test
    fun updateMe() = runTest {
        coEvery { userService.update(any()) } returns user
        coEvery { userService.findByUuid(user.uuid.toString()) } returns user

        val result = usersController.update(user.uuid.toString(), userUpdate)
        val responseDTO = result.body!!

        assertAll(
                { assertNotNull(result) },
                { assertNotNull(responseDTO) },
                { assertEquals(userResponseDTO.id, responseDTO.id) },
                { assertEquals(userResponseDTO.username, responseDTO.username) },
                { assertEquals(userResponseDTO.email, responseDTO.email) },
                { assertEquals(result.statusCode, HttpStatus.OK) },
        )
    }

    @Test
    fun updateMeFailed400() = runTest  {
        coEvery { userService.findByUuid(user.uuid.toString()) } returns user
        coEvery { userService.update(any()) } throws UserBadRequestException("Error updating the user.")

        val exception = assertThrows<ResponseStatusException> {
            usersController.update(user.uuid.toString(), userUpdate)
        }

        assertEquals("400 BAD_REQUEST \"Invalid UUID string: ${user.uuid}\"", exception.message)
    }

    @Test
    fun updateMeFailed404() = runTest  {
        coEvery { userService.findByUuid(any()) } throws UserNotFoundException("User with uuid ${user.uuid} not found.")
        coEvery { userService.update(any()) } throws UserNotFoundException("Error updating the user.")

        val exception = assertThrows<ResponseStatusException> {
            usersController.update(user.uuid.toString(), userUpdate)
        }

        assertEquals("404 NOT_FOUND \"User not found with uuid: ${user.uuid}\"", exception.message)
    }


    @Test
    fun delete() = runTest {
        coEvery { userService.deleteByUuid(any()) } returns 1

        val result = usersController.delete(user.id.toString())

        assertAll(
                { assertNotNull(result) },
                { assertEquals(result.statusCode, HttpStatus.NO_CONTENT) },
        )
    }

    @Test
    fun deleteByUuidFailed400() = runTest {
        coEvery { userService.deleteByUuid(uuidWrong.toString()) } throws UserBadRequestException("Invalid UUID string: $uuidWrong")

        val exception = assertThrows<ResponseStatusException> {
            usersController.delete(uuidWrong.toString())
        }

        assertEquals("400 BAD_REQUEST \"Invalid UUID string: ${uuidWrong}\"" , exception.message)
    }

    @Test
    fun deleteByUuidFailed404() = runTest {
        coEvery { userService.findByUuid(user.uuid.toString()) } throws UserNotFoundException("User with uuid ${user.uuid} not found.")
        coEvery { userService.deleteByUuid(user.uuid.toString()) } throws UserNotFoundException("User with uuid ${user.uuid} not found.")

        val exception = assertThrows<ResponseStatusException> {
            usersController.delete(user.uuid.toString())
        }

        assertEquals("404 NOT_FOUND \"User not found with uuid: ${user.uuid}\"", exception.message)
    }
}