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
    public JsonObject register(String username, String password ) {

        String hashedPassword = DigestUtils.sha512Hex(password);
        String checkUserSQL = "SELECT * FROM " + table + " WHERE username = ?";
        String token = RandomStringUtils.randomAlphanumeric(8);
        long token_expired = System.currentTimeMillis() + (60 * 60 * 1000); // Set token to expire in 1 hour
        try {
            JsonObject existingUser = bridge.queryOne(checkUserSQL, username);
            if (existingUser != null) {
                return SimpleResponse.createResponse(10); // Username already exists
            }

            String insertUserSQL = "INSERT INTO " + table + " (username, password, token, token_expired) VALUES (?, ?, ?, ?)";
            Object result = bridge.insert(insertUserSQL, username, hashedPassword, token, token_expired);

            if (result != null) {
                JsonObject data = new JsonObject();
                data.addProperty("username", username);
                return SimpleResponse.createResponse(0,data);
            } else {
                return SimpleResponse.createResponse(2);  // Failed to register user
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3); // An error has occurred
        }

    }


    @Override
    public JsonObject login(String username, String password) {
        String hashedPassword = DigestUtils.sha512Hex(password);
        String query = "SELECT * FROM " + table + " WHERE username = ?";

        try {
            // First, check if the user exists
            JsonObject user = bridge.queryOne(query, username);
            if (user == null) {
                return SimpleResponse.createResponse(10); // Invalid username
            }

            // If user exists, check the password
            if (!user.get("password").getAsString().equals(hashedPassword)) {
                return SimpleResponse.createResponse(11); // Incorrect password
            }

            // Generate a token and set expiration
            String token = RandomStringUtils.randomAlphanumeric(8);
            long token_expired = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour expiration

            // Update token and expiration in the database
            String updateTokenSQL = "UPDATE " + table + " SET token = ?, token_expired = ? WHERE username = ?";
            bridge.update(updateTokenSQL, token, token_expired, username);

            JsonObject data = new JsonObject();
            data.addProperty("username", user.get("username").getAsString());
            data.addProperty("token", token);
            return SimpleResponse.createResponse(0, data); // Success
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3); // An error has occurred
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
                        return SimpleResponse.createResponse(11); // The token has expired
                    } else {
                        JsonObject json = new JsonObject();
                        json.add("user", user);
                        return SimpleResponse.createResponse(0, json);
                    }
                } else {
                    return SimpleResponse.createResponse(12);  // Token expiration time is missing
                }
            } else {
                return SimpleResponse.createResponse(10); // Invalid token
            }
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(3); // An error has occurred
        }
    }


}
