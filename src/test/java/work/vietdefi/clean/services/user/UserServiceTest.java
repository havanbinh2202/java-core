package work.vietdefi.clean.services.user;



import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import work.vietdefi.clean.services.common.SimpleResponse;
import work.vietdefi.sql.HikariClient;
import work.vietdefi.sql.ISQLJavaBridge;
import work.vietdefi.sql.SqlJavaBridge;


import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for UserService.
 * Tests are run against the 'test_user' table, which is dropped after tests complete.
 */
public class UserServiceTest {


    private static ISQLJavaBridge bridge;
    private static UserService userService;
    private static final String TEST_TABLE = "test_user";


    /**
     * Set up resources before all tests.
     * Initialize the SQL bridge and create an instance of UserService.
     */
    @BeforeAll
    static void setup() throws IOException {
        HikariClient hikariClient = new HikariClient("config/sql/databases.json");
        bridge = new SqlJavaBridge(hikariClient); // Create an instance of SqlJavaBridge
        // Initialize the UserService with the test table.
        userService = new UserService(bridge, TEST_TABLE);
    }


    /**
     * Clean up resources after all tests.
     * Drop the 'test_user' table to ensure a clean slate.
     */
    @AfterAll
    static void teardown() throws Exception {
        bridge.update("DROP TABLE IF EXISTS " + TEST_TABLE);
    }


    /**
     * Test user registration.
     * Verify that a user can be successfully registered.
     */
    @Test
    void testRegisterUser() {
        String username = "testUser1";
        String password = "password123";


        JsonObject response = userService.register(username, password);

        System.out.println(response.toString());

        assertNotNull(response);
        assertTrue(SimpleResponse.isSuccess(response));
        JsonObject data = response.getAsJsonObject("d");
        assertEquals(username, data.get("username").getAsString());
        assertNotNull(data);

    }


    /**
     * Test user login.
     * Verify that a user with valid credentials can log in.
     */
    @Test
    void testLoginUser() {
        String username = "testUser";
        String password = "password123";
        userService.register(username, password);


        JsonObject response = userService.login(username, password);


        assertNotNull(response);
        assertTrue(SimpleResponse.isSuccess(response));
        JsonObject data = response.getAsJsonObject("d");
        assertEquals(username, data.get("username").getAsString());
        assertNotNull(data.get("token").getAsString());
    }


    /**
     * Test token authorization.
     * Verify that a valid token allows authorization.
     */
    @Test
    void testAuthorization() {
        String username = "testUser";
        String password = "password123";
        JsonObject loginResponse = userService.login(username, password);
        String token = loginResponse.getAsJsonObject("d").get("token").getAsString();


        JsonObject authResponse = userService.authorization(token);
        assertTrue(SimpleResponse.isSuccess(authResponse));


        JsonObject fakeResponse = userService.authorization("fake_token");
        assertFalse(SimpleResponse.isSuccess(fakeResponse));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        String username = "existingUser";
        String password = "password123";

        // First registration
        userService.register(username, password);

        // Attempt to register the same user again
        JsonObject response = userService.register(username, password);
        System.out.println(response.toString());
        // Assert response indicates username exists
        assertNotNull(response);
        assertEquals(10, response.get("e").getAsInt());  // e = 10 for username exists
    }

    @Test
    void testLoginUser_UsernameDoesNotExist() {
        String username = "nonExistentUser";
        String password = "somePassword";

        // Attempt to log in with a non-existent username
        JsonObject response = userService.login(username, password);
        System.out.println(response.toString());

        // Assert response indicates the username does not exist
        assertNotNull(response);
        assertEquals(10, response.get("e").getAsInt());  // e = 10 for invalid username
    }


    @Test
    void testLoginUser_IncorrectPassword() {
        String username = "testUser";
        String password = "password123";
        String incorrectPassword = "wrongPassword";

        // First, register the user with a valid username and password
        userService.register(username, password);

        // Attempt to log in with the correct username but incorrect password
        JsonObject response = userService.login(username, incorrectPassword);
        System.out.println(response.toString());

        // Assert response indicates invalid password
        assertNotNull(response);
        assertEquals(11, response.get("e").getAsInt());  // e = 11 for incorrect password
    }


    @Test
    void testAuthorization_InvalidToken() {
        // Attempt to authorize with an invalid token
        JsonObject response = userService.authorization("invalidToken");
        System.out.println(response.toString());
        // Assert response indicates invalid token
        assertNotNull(response);
        assertEquals(10, response.get("e").getAsInt());  // e = 10 for invalid token
    }

    @Test
    void testAuthorization_ExpiredToken() throws Exception {
        // Simulate the case of expired tokens
        String token = "token_expired"; // Make sure this token has been added to the database
        long expiredTime = System.currentTimeMillis() - (60 * 60 * 1000); // time expired 1 hour ago

        // Add expired token to database
        String insertExpiredTokenSQL = "INSERT INTO " + TEST_TABLE + " (username, token, token_expired) VALUES (?, ?, ?)";
        bridge.insert(insertExpiredTokenSQL, "test_user", token, expiredTime); // Make sure username is included

        // Call authorization method with expired token
        JsonObject response = userService.authorization(token);
        System.out.println(response.toString());

        // Check if the response indicates the token has expired
        assertNotNull(response);
        assertEquals(11, response.get("e").getAsInt());  // e = 11 Token has expired
    }



}
