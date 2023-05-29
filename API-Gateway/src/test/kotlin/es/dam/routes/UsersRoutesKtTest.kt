package es.dam.routes

import es.dam.dto.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

private val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersRoutesKtTest{

    private val loginAdminDTO = UserLoginDTO(
        username = "TesTiNg-admin",
        password = "admin1234"
    )

    private val loginNotAdminDTO = UserLoginDTO(
        username = "TesTiNg-user",
        password = "user1234"
    )

    private val loginInactive = UserLoginDTO(
        username = "inactive",
        password = "inactive1234"
    )

    private var userAdminId = ""
    private var userNotAdminId = ""
    private var registerTestId = ""
    private var userInactiveId = ""
    private var userDeleteId = ""

    private val wrongUUID = "00000000-0000-0000-0000-000000000000"

    @BeforeAll
    fun setUp() = testApplication{
        val registerDTO = UserRegisterDTO(
            name = "TesTiNg-admin",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            userRole = setOf("ADMINISTRATOR"),
            isActive = true
        )

        val registerNotAdminDTO = UserRegisterDTO(
            name = "TesTiNg-user",
            username = "TesTiNg-user",
            email = "TesTiNguser@email.com",
            password = "user1234",
            userRole = setOf("USER"),
            isActive = true
        )

        val registerInactiveDTO = UserRegisterDTO(
            name = "inactive",
            username = "inactive",
            email = "inactive@email.com",
            password = "inactive1234",
            userRole = setOf("USER"),
            isActive = false
        )

        val registerDeleteDTO = UserRegisterDTO(
            name = "delete",
            username = "delete",
            email = "delete@email.com",
            password = "delete1234",
            userRole = setOf("USER"),
            isActive = true
        )

        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }

        val register = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerNotAdminDTO)
        }

        userInactiveId = json.decodeFromString<UserTokenDTO>(register.bodyAsText()).user.uuid

        val register2 = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDeleteDTO)
        }

        userDeleteId = json.decodeFromString<UserTokenDTO>(register2.bodyAsText()).user.uuid

        client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerInactiveDTO)
        }

        val loginAdmin = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(loginAdmin.bodyAsText())

        userAdminId = userTokenDTO.user.uuid

        val loginNotAdmin = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val userTokenNotAdminDTO = json.decodeFromString<UserTokenDTO>(loginNotAdmin.bodyAsText())

        userNotAdminId = userTokenNotAdminDTO.user.uuid
    }

    @AfterAll
    fun tearDown() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        client.delete("/users/$userAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/users/$userNotAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/users/$registerTestId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        client.delete("/users/$userInactiveId") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }
    }

    @Test
    fun login() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        assertEquals(HttpStatusCode.OK, login.status)
    }

    @Test
    fun login401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginInactive)
        }

        assertEquals(HttpStatusCode.Unauthorized, login.status)
    }

    @Test
    fun login404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(UserLoginDTO(
                username = "notFound",
                password = "notFound1234"
            ))
        }

        assertEquals(HttpStatusCode.NotFound, login.status)
    }

    @Test
    fun register() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerTest = UserRegisterDTO(
            name = "registerTest",
            username = "registerTest",
            email = "registerTest",
            password = "registerTest",
            userRole = setOf("ADMIN"),
            isActive = true
        )


        val result = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerTest)
        }

        registerTestId = json.decodeFromString<UserTokenDTO>(result.bodyAsText()).user.uuid

        assertEquals(HttpStatusCode.Created, result.status)
    }

    /*@Test
    fun register400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val registerTest = UserRegisterDTO(
            name = "registerTest",
            username = "registerTest",
            email = "registerTest",
            password = "1",
            userRole = setOf("ADMIN"),
            isActive = true
        )


        val result = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerTest)
        }

        registerTestId = json.decodeFromString<UserTokenDTO>(result.bodyAsText()).user.uuid

        assertEquals(HttpStatusCode.BadRequest, result.status)
    }

     */

    @Test
    fun getAll() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        val result = json.decodeFromString<UserDataDTO>(response.bodyAsText())

        assertTrue(result.data!!.isNotEmpty())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getAllNotAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val userTokenDTO = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users") {
            header(HttpHeaders.Authorization, "Bearer " + userTokenDTO.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getById() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/${userAdminId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getById400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun getById404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/$wrongUUID") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun getById401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/${userAdminId}") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun getMeAdmin() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getMeUser() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        val response = client.get("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun put() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            avatar = "",
            userRole = setOf("ADMINISTRATOR"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/$userAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    /*@Test
    fun put401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            avatar = "",
            userRole = setOf("ADMINISTRATOR"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/$userAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

     */

    @Test
    fun put404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            avatar = "",
            userRole = setOf("ADMINISTRATOR"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/$wrongUUID") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun put400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            avatar = "",
            userRole = setOf("ADMINISTRATOR"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun putMe() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-admin",
            email = "TesTiNgadmin@email.com",
            password = "admin1234",
            avatar = "",
            userRole = setOf("ADMINISTRATOR"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun putMeUser() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())

        //TODO: la contraseña se cifra?
        val userUpdateDTO = UserUpdateDTO(
            name = "updated",
            username = "TesTiNg-user",
            email = "TesTiNguser@email.com",
            password = "user1234",
            avatar = "",
            userRole = setOf("USER"),
            credits = 20,
            isActive = true
        )

        val response = client.put("/users/me") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
            setBody(userUpdateDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun putCredits() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/credits/$userAdminId/4") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun putCredits401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/credits/$userAdminId/4") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun putCredits404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/credits/$wrongUUID/4") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun putCredits400A() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/credits/123/4") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    /*@Test
    fun putCredits400B() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/credits/$userAdminId/cuatro") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

     */

    @Test
    fun putActive() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/active/$userNotAdminId/true") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun putActive401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/active/$userNotAdminId/true") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun putActive404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/active/$wrongUUID/true") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun putActive400A() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/active/123/true") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun putActive400B() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.put("/users/active/$userNotAdminId/verdadero") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun delete() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.delete("/users/$userDeleteId") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun delete401() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginNotAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.delete("/users/$userNotAdminId") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    /*@Test
    fun delete400() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.delete("/users/123") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

     */

    @Test
    fun delete404() = testApplication {
        environment { config }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val login = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(loginAdminDTO)
        }

        val dto = json.decodeFromString<UserTokenDTO>(login.bodyAsText())


        val response = client.delete("/users/$wrongUUID") {
            header(HttpHeaders.Authorization, "Bearer " + dto.token)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}