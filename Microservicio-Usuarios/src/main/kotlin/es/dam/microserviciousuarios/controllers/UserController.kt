package es.dam.microserviciousuarios.controllers

import es.dam.microserviciousuarios.dto.*
import es.dam.microserviciousuarios.exceptions.UserBadRequestException
import es.dam.microserviciousuarios.exceptions.UserNotFoundException
import es.dam.microserviciousuarios.mappers.toDTO
import es.dam.microserviciousuarios.mappers.toModel
import es.dam.microserviciousuarios.models.User
import es.dam.microserviciousuarios.service.UserService
import es.dam.microserviciousuarios.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UsersController @Autowired constructor(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JWTUtils
) {
    @PostMapping("/login")
    fun login(@RequestBody logingDto: UserLoginDTO): ResponseEntity<UserTokenDTO> {
        try {

            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    logingDto.username,
                    logingDto.password
                )
            )

            SecurityContextHolder.getContext().authentication = authentication

            val user = authentication.principal as User

            val jwtToken: String = jwtUtils.generateToken(user)

            return ResponseEntity.ok(UserTokenDTO(user.toDTO(), jwtToken))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password")
        }
    }

    @PostMapping("/register")
    suspend fun register(@RequestBody usuarioDto: UserRegisterDTO): ResponseEntity<UserTokenDTO> {
        try {
            val user = usuarioDto.toModel()
            val userInsert = userService.save(user)
            val jwtToken: String = jwtUtils.generateToken(userInsert)

            return ResponseEntity.ok(UserTokenDTO(userInsert.toDTO(), jwtToken))
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists with that username or email")
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("")
    suspend fun findAll(@AuthenticationPrincipal user: User): ResponseEntity<UserDataDTO> {
        try {
            val res = userService.findAll().toList().map { it.toDTO() }
            val res2 = UserDataDTO(res)
            return ResponseEntity.ok(res2)
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found")
        }
    }

    //@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<UserResponseDTO> {
        try {
            val res = userService.findByUuid(id).toDTO()
            return ResponseEntity.ok(res)
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: $id")
        }
    }

    @GetMapping("/me/{id}")
    suspend fun findMe(@PathVariable id: String): ResponseEntity<UserResponseDTO> {
        try {
            val res = userService.findByUuid(id).toDTO()
            return ResponseEntity.ok(res)
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: $id")
        }
    }

    @GetMapping("/isActive/{username}")
    suspend fun isActive(@PathVariable username: String): ResponseEntity<Boolean> {
        try {
            val res = userService.isActive(username)
            return ResponseEntity.ok(res)
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: $username")
        }
    }

    @PutMapping("/active/{id}/{active}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    suspend fun updateActive(
        @PathVariable id: String,
        @PathVariable active: Boolean
    ): ResponseEntity<UserResponseDTO> {
        try {
            val updated = userService.findByUuid(id)
            val res = userService.update(updated.copy(isActive = active))
            return ResponseEntity.status(HttpStatus.OK).body(res?.toDTO())
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: $id")
        }
    }

    @PutMapping("/me")
    suspend fun update(
        @AuthenticationPrincipal user: User,
        @RequestBody userDTO: UserUpdateDTO
    ): ResponseEntity<UserResponseDTO> {
        try {
            val updated = user.copy(
                avatar = userDTO.avatar
            )
            val res = userService.update(updated)

            return ResponseEntity.status(HttpStatus.OK).body(res?.toDTO())
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: ${user.uuid}")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: ${user.uuid}")
        }
    }

    @PutMapping("/credits/me/{id}/{creditsAmount}")
    suspend fun updateCreditsMe(
        @PathVariable id: String,
        @PathVariable creditsAmount: Int
    ): ResponseEntity<UserResponseDTO> {
        try {
            val updated = userService.findByUuid(id)
            val updatedCredits = updated.credits - creditsAmount
            val res = userService.update(updated.copy(credits = updatedCredits))
            return ResponseEntity.status(HttpStatus.OK).body(res?.toDTO())
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID or credits amount")
        }
    }

    @PutMapping("/credits/{id}/{creditsAmount}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    suspend fun updateCredits(
        @PathVariable id: String,
        @PathVariable creditsAmount: Int
    ): ResponseEntity<UserResponseDTO> {
        try {
            val updated = userService.findByUuid(id)
            val updatedCredits = updated.credits - creditsAmount
            val res = userService.update(updated.copy(credits = updatedCredits))
            return ResponseEntity.status(HttpStatus.OK).body(res?.toDTO())
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID or credits amount")
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: String,
        @RequestBody userDTO: UserUpdateDTO
    ): ResponseEntity<UserResponseDTO> {
        try {
            val updated = userService.findByUuid(id).copy(
                name = userDTO.name,
                username = userDTO.username,
                isActive = userDTO.isActive,
                credits = userDTO.credits,
                userRole = userDTO.userRole.toString().replace("[" , "").replace("]" , ""),
                avatar = userDTO.avatar
            )
            val res = userService.update(updated)

            return ResponseEntity.status(HttpStatus.OK).body(res?.toDTO())
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: $id")
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<UserDTO> {
        try {
            userService.deleteByUuid(id)
            return ResponseEntity.noContent().build()
        } catch (e: UserNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with uuid: $id")
        } catch (e: UserBadRequestException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID string: $id")
        }
    }
}