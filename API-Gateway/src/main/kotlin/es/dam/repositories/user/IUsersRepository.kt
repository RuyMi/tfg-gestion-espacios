package es.dam.repositories.user

import es.dam.dto.*

interface IUsersRepository {
    suspend fun findAll(token: String): UserDataDTO
    suspend fun findById(token: String, id: Long): UserResponseDTO
    suspend fun update(token: String, id: Long, entity: UserUpdateDTO): UserResponseDTO
    suspend fun delete(token: String, id: Long)
    suspend fun login(entity: UserLoginDTO): UserTokenDTO
    suspend fun register(entity: UserRegisterDTO): UserTokenDTO
}