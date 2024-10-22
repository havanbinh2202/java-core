package work.vietdefi.clean.services.user;

import com.google.gson.JsonObject;
import work.vietdefi.sql.ISQLJavaBridge;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import work.vietdefi.clean.services.common.SimpleResponse;
import work.vietdefi.util.log.DebugLogger;

import java.math.BigInteger;

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
        try {
            String query = new StringBuilder()
                    .append("SELECT user_id FROM ")
                    .append(table)
                    .append(" WHERE username = ?").toString();
            JsonObject data = bridge.queryOne(query, username);
            if(data != null){
                return SimpleResponse.createResponse(10);
            }
            String  hashedPassword = DigestUtils.sha512Hex(password);
            String token = RandomStringUtils.randomAlphanumeric(8);
            long TOKEN_EXPIRED = 0;
            long token_expired = System.currentTimeMillis() + TOKEN_EXPIRED;
            query = new StringBuilder()
                    .append("INSERT INTO ")
                    .append(table)
                    .append(" (username, password, token, token_expired)")
                    .append(" VALUE (?,?,?,?)")
                    .toString();
            BigInteger generatedKey = (BigInteger) bridge.insert(query, username, hashedPassword, token, token_expired);
            long id = generatedKey.longValue();
            data = new JsonObject();
            data.addProperty("user_id", id);
            data.addProperty("username", username);
            data.addProperty("token", token);
            data.addProperty("token_expired", token_expired);
            return SimpleResponse.createResponse(0, data);
        }catch (Exception e){
            DebugLogger.logger.error("", e);
            return SimpleResponse.createResponse(1);
        }
    }


    @Override
    public JsonObject login(String username, String password) {
        try {
            String query = new StringBuilder()
                    .append(" SELECT * FROM ")
                    .append(table)
                    .append(" WHERE username = ? ")
                    .toString();
            JsonObject user = bridge.queryOne(query, username);
            if (user == null) {
                return SimpleResponse.createResponse(10);
            }

            String hashedPassword = DigestUtils.sha512Hex(password);
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
            data.addProperty("token_expired", token_expired);
            return SimpleResponse.createResponse(0, data); // Success
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleResponse.createResponse(1); // An error has occurred
        }
    }



    @Override
    public JsonObject authorization(String token) {
        try{
            String query = new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE token = ?")
                    .toString();
            JsonObject user = bridge.queryOne(query, token);
            if (user == null) {
                return SimpleResponse.createResponse(10); // Invalid token
            }
                long tokenExpired = user.get("token_expired").getAsLong();
                if (System.currentTimeMillis() > tokenExpired) {
                    return SimpleResponse.createResponse(11); // The token has expired
                }
                JsonObject json = new JsonObject();
                json.add("user", user);
                return SimpleResponse.createResponse(0, json);
        } catch (Exception e) {
            DebugLogger.logger.error("", e);
            return SimpleResponse.createResponse(1); // An error has occurred
        }
    }


}
