package es.dam.services.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import es.dam.config.TokenConfig
import io.ktor.server.auth.jwt.*
import org.koin.core.annotation.Single

@Single
class TokensService(
    private val tokenConfig: TokenConfig
) {

    fun verifyJWT(): JWTVerifier {
        return JWT.require(Algorithm.HMAC512(tokenConfig.secret))
            .withIssuer(tokenConfig.issuer)
            .build()
    }

    fun generateToken(token: JWTPrincipal): String{
        return JWT.create()
            .withIssuer(token.payload.issuer)
            .withSubject(token.payload.subject)
            .withClaim("username", token.payload.getClaim("username").toString().replace("\"", ""))
            .withClaim("role", token.payload.getClaim("role").toString().replace("\"", ""))
            .withExpiresAt(token.payload.expiresAt)
            .sign(Algorithm.HMAC512("reservas-luisvives"))
    }
}