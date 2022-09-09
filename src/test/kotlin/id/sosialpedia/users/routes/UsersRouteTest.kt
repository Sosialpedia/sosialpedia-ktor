package id.sosialpedia.users.routes

/**
 * @author Samuel Mareno`
 * @Date 26/08/22
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//class UsersRouteTest {
//    private var fakeUserId = "test123456712345"
//
//    @Test
//    fun `1 Test register user`() = testApplication {
//        val response = client.post("/user/register") {
//            contentType(ContentType.Application.Json)
//            setBody(
//                Json.encodeToString(
//                    CreateUserRequest(
//                        id = fakeUserId,
//                        username = "testUsername",
//                        email = "test@email.com",
//                        password = "test",
//                        phoneNumber = "32847932847",
//                        profilePic = "testPic",
//                        bio = "test bio",
//                        dateBirth = System.currentTimeMillis(),
//                        gender = "Male",
//                        lastLogin = System.currentTimeMillis(),
//                        ipAddress = "testIp",
//                        device = "Test Device"
//                    )
//                )
//            )
//        }
//        assertEquals(HttpStatusCode.Created, response.status)
//    }
//
//    @Test
//    fun `2 Test find user with specific Id`() = testApplication {
//        val id = fakeUserId
//        val response = client.get("/user/${id}")
//        val user = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
//        assertEquals(HttpStatusCode.OK, response.status)
//        assertEquals(user.data[0].id, id)
//    }
//
//
//    @Test
//    fun `3 Test get all users`() = testApplication {
//        val response = client.get("/users")
//        val users = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
//        assertEquals(HttpStatusCode.OK, response.status)
//        assertTrue(users.data.isNotEmpty())
//    }
//
//    @Test
//    fun `4 Test find user is not found`() = testApplication {
//        val response = client.get("/user/nothing")
//        val user = Json.decodeFromString<WebResponse<List<User>>>(response.bodyAsText())
//        assertEquals(HttpStatusCode.OK, response.status)
//        assertTrue(user.data.isEmpty())
//    }
//
//    @Test
//    fun `5 Test update login detail`() = testApplication {
//        val response = client.put("user/upd4t3l0g1n/${fakeUserId}")
//        assertEquals("operation: true", response.bodyAsText())
//    }
//
//    @Test
//    fun `6 Test delete user with specific id`() = testApplication {
//        val response = client.delete("/user/${fakeUserId}")
//        assertEquals("User deleted", response.bodyAsText())
//    }
//
//}