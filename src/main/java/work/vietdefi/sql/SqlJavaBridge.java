package work.vietdefi.sql;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;


/**
 * SqlJavaBridge provides an implementation of the ISQLJavaBridge interface,
 * allowing interaction with SQL databases using HikariCP for connection management.
 * This class includes methods for checking table existence, creating tables,
 * executing queries, and converting query results to JSON format.
 */
public class SqlJavaBridge implements ISQLJavaBridge {
    // HikariClient instance used to manage database connections efficiently.
    private final HikariClient hikariClient;


    // Constructor to initialize the HikariClient with the given configuration file path.
    public SqlJavaBridge(HikariClient hikariClient) {
        this.hikariClient = hikariClient;
    }


    /**
     * Checks if a specified table exists in the database.
     *
     * @param table The name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    @Override
    public boolean checkTableExisting(String table) {
        String query = "SHOW TABLES LIKE ?";
        try (Connection connection = hikariClient.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, table);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            // Log the exception here
            e.printStackTrace();
            return false;
        }
    }




    /**
     * Creates a new table in the database and optionally creates indexes for it.
     * This method executes the provided SQL statement to create a table.
     * If index creation SQL statements are provided, it will execute those
     * as well. The entire operation is performed within a transaction.
     * If any part of the process fails, the transaction is rolled back
     * to ensure database integrity.
     *
     * @param createTableSQL The SQL statement used to create the table.
     * @param createIndexSql Optional SQL statements to create indexes on the table.
     * @return true if the table and indexes are created successfully,
     *         false if an error occurs during the operation.
     */
    @Override
    public boolean createTable(String createTableSQL, String... createIndexSql) {
        Connection connection = null; // Declare connection here
        try {
            connection = hikariClient.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
                preparedStatement.executeUpdate();
            }

            for (String indexSql : createIndexSql) {
                try (PreparedStatement indexStatement = connection.prepareStatement(indexSql)) {
                    indexStatement.executeUpdate();
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(); // Use the existing connection variable
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close(); // Ensure the connection is closed
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    /**
     * Converts a single row from the ResultSet to a JsonObject.
     *
     * @param resultSet The ResultSet containing the query result.
     * @return A JsonObject representing the row data.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    @Override
    public JsonObject convertResultSetToJsonObject(ResultSet resultSet) throws SQLException {
        JsonObject jsonObject = new JsonObject();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++){
            jsonObject.addProperty(metaData.getColumnName(i),resultSet.getString(i));
        }
        return jsonObject;
    }


    /**
     * Converts the entire ResultSet to a JsonArray, where each row is represented as a JsonObject.
     *
     * @param resultSet The ResultSet containing multiple rows of query results.
     * @return A JsonArray representing the query result.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    @Override
    public JsonArray convertResultSetToJsonArray(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray = new JsonArray();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()){
            JsonObject jsonObject = new JsonObject();
            for (int i = 1; i <= columnCount; i++){
                jsonObject.addProperty(metaData.getColumnName(i), resultSet.getString(i));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }



    /**
     * Executes a query that is expected to return a single row and converts the result to a JsonObject.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return A JsonObject representing the single row result, or null if no result is found.
     * @throws Exception If the query execution or conversion fails.
     */
    @Override
    public JsonObject queryOne(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            setParameters(preparedStatement, params);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return convertResultSetToJsonObject(resultSet);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Executes a query that returns multiple rows and converts the result to a JsonArray.
     *
     * @param query  The SQL query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return A JsonArray containing all rows of the query result.
     * @throws Exception If the query execution or conversion fails.
     */
    @Override
    public JsonArray queryArray(String query, Object... params) throws Exception{
        try (Connection connection = hikariClient.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);

            ResultSet resultSet = preparedStatement.executeQuery();
            return convertResultSetToJsonArray(resultSet);

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Executes an INSERT statement and returns the generated key.
     *
     * @param query  The SQL INSERT query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return The generated key from the insert operation (e.g., auto-increment ID), or null if no key is generated.
     * @throws Exception If the insertion or key retrieval fails.
     */
    @Override
    public Object insert(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)){
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                return resultSet.getObject(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Executes an UPDATE or DELETE statement and returns the number of rows affected.
     *
     * @param query  The SQL UPDATE/DELETE query to execute.
     * @param params The parameters to bind to the query, if any.
     * @return The number of rows affected by the operation.
     * @throws Exception If the query execution fails.
     */
    @Override
    public int update(String query, Object... params) throws Exception {
        try (Connection connection = hikariClient.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setParameters(preparedStatement, params);
            return preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * Sets the parameters for a PreparedStatement.
     *
     * @param statement The PreparedStatement to set parameters on.
     * @param params    The parameters to bind to the statement.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        // Loop through each parameter and set it in the PreparedStatement
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]); // Set parameter at index (i + 1) because PreparedStatement is 1-indexed
        }
    }


    /**
     * Closes the HikariClient and releases all database connections.
     * <p>
     * This method should be called when the SqlJavaBridge instance is no longer needed
     * to ensure that all database connections are closed and resources are released.
     * Failure to call this method may result in memory leaks or exhaustion of database connections.
     * </p>
     */
    public void close() {
        if (hikariClient != null) {
            hikariClient.close(); // Close the HikariClient to release resources
        }
    }
}
