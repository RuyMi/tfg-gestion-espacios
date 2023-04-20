package es.dam.microserviciousuarios.models

import es.dam.microserviciousuarios.serializers.LocalDateTimeSerializer
import es.dam.microserviciousuarios.serializers.UUIDSerializer
import jakarta.validation.constraints.*
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Serializable
data class User(
    @Id
    val id: Long? = null,
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    @NotNull @NotBlank(message = "El nombre no puede estar vacío.") @NotEmpty(message = "El nombre no puede estar vacío.")
    val name: String,
    @NotNull @NotBlank(message = "El nombre de usuario no puede estar vacío.") @NotEmpty(message = "El nombre de usuario no puede estar vacío.")
    val username: String,
    @NotNull @NotBlank(message = "El email no puede estar vacío.") @NotEmpty(message = "El email no puede estar vacío.") @Email(
        regexp = "^[A-Za-z0-9+_.-]+@(.+)\$",
        message = "Email no válido."
    )
    val email: String,
    @NotNull @NotBlank(message = "La contraseña no puede estar vacía.") @NotEmpty(message = "La contraseña no puede estar vacía.") @Min(
        8,
        message = "La contraseña debe tener al menos 8 caracteres."
    )
    val password: String,
    val avatar: String? = null,
    val userRole: String = UserRole.USER.name,

    // Históricos.
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastPasswordChangeAt: LocalDateTime = LocalDateTime.now()
) : UserDetails {
    enum class UserRole {
        USER, TEACHER, ADMINISTRADOR
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return userRole.split(",").map { SimpleGrantedAuthority("ROLE_${it.trim()}") }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
