package id.sosialpedia.users.routes

import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.util.WebResponse
import id.sosialpedia.util.toShuffledMD5
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Samuel Mareno`
 * @Date 26/08/22
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UsersRouteTest {

    private var fakeUserId = UUID.randomUUID().toShuffledMD5(15)

    @Test
    fun `1 Test get all users`() = testApplication {
        val response = client.get("/users")
        val users = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(users.data.isNotEmpty())
    }

    @Test
    fun `2 Test find user with specific Id`() = testApplication {
        val id = "7afc74f52a870326"
        val response = client.get("/user/${id}")
        val user = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(user.data[0].id, id)
    }

    @Test
    fun `3 Test find user is not found`() = testApplication {
        val response = client.get("/user/${fakeUserId}")
        val user = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(user.data.isEmpty())
    }

    @Test
    fun `Test register user`() = testApplication {
//        val client = createClient {
//            install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                    prettyPrint = true
//                    isLenient = true
//                })
//            }
//        }
        val response = client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(
                Json.encodeToString(
                    CreateUserRequest(
                        username = "testUsername",
                        email = "test@email.com",
                        password = "test",
                        phoneNumber = "32847932847",
                        profilePic = "testPic",
                        bio = "test bio",
                        dateBirth = System.currentTimeMillis(),
                        gender = "male",
                        lastLogin = System.currentTimeMillis(),
                        ipAddress = "testIp",
                        device = "Test Device"
                    )
                )
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}