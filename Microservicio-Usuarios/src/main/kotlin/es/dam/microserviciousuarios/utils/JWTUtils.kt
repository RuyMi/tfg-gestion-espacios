package es.dam.microserviciousuarios.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import es.dam.microserviciousuarios.models.User
import org.springframework.stereotype.Component
import java.util.*

/**
 * Clase de utilidades de JWT. Se encarga de generar y verificar tokens JWT.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Component
class JWTUtils {
    /**
     * Genera un token JWT a partir de un usuario.
     *
     * @param user Usuario del que se quiere generar el token.
     * @return Token JWT generado.
     */
    fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(user.uuid.toString())
            .withIssuer("luisvives")
            .withExpiresAt(Date(System.currentTimeMillis() + (4 * 60 * 60 * 1000)))
            .withClaim("username", user.username)
            .withClaim("role", user.userRole.split(",").toSet().toString())
            .sign(Algorithm.HMAC512("reservas-luisvives"))
    }

    /**
     * Verifica un token JWT.
     *
     * @param authToken Token JWT a verificar.
     * @return DecodedJWT
     */
    fun verify(authToken: String): DecodedJWT? {
        return try {
            JWT.require(Algorithm.HMAC512("reservas-luisvives")).build().verify(authToken)
        } catch (e: Exception) {
            null
        }
    }
}