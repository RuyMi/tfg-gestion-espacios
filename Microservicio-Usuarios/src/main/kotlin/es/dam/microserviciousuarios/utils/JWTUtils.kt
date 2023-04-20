package es.dam.microserviciousuarios.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import es.dam.microserviciousuarios.models.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtils {
    fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(user.uuid.toString())
            .withIssuer("luisvives")
            .withExpiresAt(Date(System.currentTimeMillis() + (60 * 60 * 1000)))
            .withClaim("username", user.username)
            .withClaim("rol", user.userRole.split(",").toSet().toString())
            .sign(Algorithm.HMAC512("BiquesDAM"))
    }

    fun verify(authToken: String): DecodedJWT? {
        return try {
            JWT.require(Algorithm.HMAC512("BiquesDAM")).build().verify(authToken)
        } catch (e: Exception) {
            null
        }
    }
}