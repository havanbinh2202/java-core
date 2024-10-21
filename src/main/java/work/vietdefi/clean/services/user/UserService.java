package work.vietdefi.clean.services.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import work.vietdefi.sql.ISQLJavaBridge;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import work.vietdefi.clean.services.common.SimpleResponse;

public class UserService implements IUserService {

    private ISQLJavaBridge bridge;
    private String table;

    public UserService(ISQLJavaBridge bridge, String table) {
        this.bridge = bridge;
        this.table = table;

        if (!bridge.checkTableExisting(table)) {
            createTable();
        }
    }

    private void createTable() {
        String createTableSQL = ("CREATE TABLE IF NOT EXISTS table_name (" +
                "user_id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(64) UNIQUE," +
                "password VARCHAR(2048)," +
                "token VARCHAR(2048)," +
                "token_expired BIGINT DEFAULT 0" +
                ")").replace("table_name", table);

        String indexSQL1 = "CREATE UNIQUE INDEX table_name_username_uindex ON table_name (username)"
                .replace("table_name", table);
        String indexSQL2 = "CREATE INDEX table_name_token_index ON table_name (token)"
                .replace("table_name", table);

        bridge.createTable(createTableSQL, indexSQL1, indexSQL2);
    }

    @Override
    public JsonObject register(String username, String password) {
        String hashedPassword = DigestUtils.sha512Hex(password);
        String checkUserSQL = "SELECT * FROM " + table + " WHERE username = ?";

        try {
            JsonObject existingUser = bridge.queryOne(checkUserSQL, username);
            if (existingUser != null) {
                    return SimpleResponse.createResponse(10, createMessageJson("Username already exists"));
            }

            String insertUserSQL = "INSERT INTO " + table + " (username, password) VALUES (?, ?)";
            Object result = bridge.insert(insertUserSQL, username, hashedPassword);

            if (result != null) {
                return SimpleResponse.createResponse(0, createUserJson(username));
            } else {
                return SimpleResponse.createResponse(2, createMessageJson("Failed to register user"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3, createMessageJson("An error has occurred: " + e.getMessage()));
        }

    }


    @Override
    public JsonObject login(String username, String password) {
        String hashedPassword = DigestUtils.sha512Hex(password);
        String query = "SELECT * FROM " + table + " WHERE username = ?";

        try {
            JsonObject user = bridge.queryOne(query, username);

            // Check if the user exists
            if (user == null) {
                // User not found, return error for invalid username
                return SimpleResponse.createResponse(10, createMessageJson("Invalid username"));
            }

            // Check if the password matches
            if (!user.get("password").getAsString().equals(hashedPassword)) {
                // Password does not match, return error for invalid password
                return SimpleResponse.createResponse(11, createMessageJson("Invalid password"));
            }

            // If authentication is successful, generate a token
            String token = RandomStringUtils.randomAlphanumeric(8);
            long tokenExpired = System.currentTimeMillis() + (60 * 60 * 1000); // Set token to expire in 1 hour

            String updateTokenSQL = "UPDATE " + table + " SET token = ?, token_expired = ? WHERE username = ?";
            bridge.update(updateTokenSQL, token, tokenExpired, username);

            JsonObject data = createUserJson(username, token);
            return SimpleResponse.createResponse(0, data);
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3, createMessageJson("An error has occurred: " + e.getMessage()));
        }
    }

    @Override
    public JsonObject authorization(String token) {
        String query = "SELECT * FROM " + table + " WHERE token = ?";
        try {
            JsonObject user = bridge.queryOne(query, token);
            if (user != null) {
                if (user.has("token_expired")) {
                    long tokenExpired = user.get("token_expired").getAsLong();
                    if (System.currentTimeMillis() > tokenExpired) {
                        return SimpleResponse.createResponse(11, createMessageJson("The token has expired"));
                    } else {
                        JsonObject data = new JsonObject();
                        data.add("user", user);
                        return SimpleResponse.createResponse(0, data);
                    }
                } else {
                    return SimpleResponse.createResponse(12, createMessageJson("Token expiration time is missing"));
                }
            } else {
                return SimpleResponse.createResponse(10, createMessageJson("Invalid token"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3, createMessageJson("An error has occurred: " + e.getMessage()));
        }
    }


    // Helper method to create message JsonObject
    private JsonObject createMessageJson(String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        return jsonObject;
    }

    // Helper method to create user JsonObject
    private JsonObject createUserJson(String username) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        return jsonObject;
    }

    // Overloaded method to create user JsonObject with token
    private JsonObject createUserJson(String username, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("token", token);
        return jsonObject;
    }
}
