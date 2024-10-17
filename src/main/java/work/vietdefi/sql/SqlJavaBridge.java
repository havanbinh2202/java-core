package work.vietdefi.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Properties;

public class SqlJavaBridge {
    private Connection connection;

    public SqlJavaBridge(HikariClient hikariClient) throws SQLException {
        this.connection = hikariClient.getConnection(); // Get connection from HikariClient
    }

    /**
     * Check the existence of a table in the database.
     *
     * @param tableName Name of the table to check.
     * @return true if the table exists, false otherwise.
     * @throws SQLException If an error occurs during the query.
     */
    public boolean checkTableExisting(String tableName) throws SQLException {
        ResultSet resultSet = null;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
            return resultSet.next(); // If there is a result, the table exists

        } finally {
            if (resultSet != null) {
                resultSet.close(); // Make sure to close ResultSet

            }
        }
    }

    /**
     * Create a table in the database.
     *
     * @param createTableSQL SQL statement to create table.
     * @return true if the table was created successfully, false otherwise.
     * @throws SQLException If an error occurs during command execution.
     */
    public boolean createTable(String createTableSQL) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(createTableSQL) == 0; // If there are no errors, return true

        }
    }

    /**
     * Inserts a record into the table and returns the generated primary key.
     *
     * @param insertSQL SQL statement to insert records.
     * @param params Parameters for the insert statement.
     * @return Primary key created, or null if failed.
     * @throws SQLException If an error occurred during execution.
     */
    public Object insert(String insertSQL, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getObject(1); // Returns the generated primary key

            } else {
                return null; // No primary key is created

            }
        }
    }

    /**
     * Update a record in the table.
     *
     * @param updateSQL SQL statement to update the record.
     * @param params Parameters for the update command.
     * @return Number of rows affected.
     * @throws SQLException If an error occurred during execution.
     */
    public int update(String updateSQL, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate(); // Returns the number of rows affected

        }
    }

    /**
     * Query a record from a table based on ID.
     *
     * @param querySQL SQL statement to query records.
     * @param params Parameters for the query statement.
     * @return The JsonObject representing the record.
     * @throws SQLException If an error occurred during execution.
     */
    public JsonObject queryOne(String querySQL, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(querySQL)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    jsonObject.addProperty(metaData.getColumnName(i), resultSet.getString(i));
                }
                return jsonObject; // Returns the JsonObject object

            }
        }
        return null; // If no records are found

    }

    /**
     * Query all records from the table.
     *
     * @param querySQL SQL statement to query.
     * @return JsonArray containing all records.
     * @throws SQLException If an error occurred during execution.
     */
    public JsonArray queryArray(String querySQL) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(querySQL)) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JsonObject jsonObject = new JsonObject();
                for (int i = 1; i <= columnCount; i++) {
                    jsonObject.addProperty(metaData.getColumnName(i), resultSet.getString(i));
                }
                jsonArray.add(jsonObject); // Add objects to JsonArray

            }
        }
        return jsonArray; // Returns a JsonArray containing all records

    }

    /**
     * Close the connection to the database.
     *
     * @throws SQLException If an error occurred while closing the connection.
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Close the connection

        }
    }
}
