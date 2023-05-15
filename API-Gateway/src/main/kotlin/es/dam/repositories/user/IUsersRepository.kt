package es.dam.repositories.user

import es.dam.dto.*

interface IUsersRepository {
    suspend fun findAll(token: String): UserDataDTO
    suspend fun findById(token: String, id: String): UserResponseDTO
    suspend fun isActive(username: String): Boolean
    suspend fun update(token: String, id: String, entity: UserUpdateDTO): UserResponseDTO
    suspend fun updateCredits(token: String, id: String, creditsAmount: Int): UserResponseDTO
    suspend fun updateActive(token: String, id: String, active: Boolean): UserResponseDTO
    suspend fun delete(token: String, id: String)
    suspend fun me(token: String, entity: UserUpdateDTO): UserResponseDTO
    suspend fun login(entity: UserLoginDTO): UserTokenDTO
    suspend fun register(entity: UserRegisterDTO): UserTokenDTO
}