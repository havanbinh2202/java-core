package work.vietdefi.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Properties;

public class SqlJavaBridge {
    private Connection connection;

    public SqlJavaBridge(HikariClient hikariClient) throws SQLException {
        this.connection = hikariClient.getConnection(); // Lấy kết nối từ HikariClient
    }

    /**
     * Kiểm tra sự tồn tại của một bảng trong cơ sở dữ liệu.
     *
     * @param tableName Tên của bảng cần kiểm tra.
     * @return true nếu bảng tồn tại, false nếu không.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình truy vấn.
     */
    public boolean checkTableExisting(String tableName) throws SQLException {
        ResultSet resultSet = null;
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
            return resultSet.next(); // Nếu có kết quả, bảng tồn tại
        } finally {
            if (resultSet != null) {
                resultSet.close(); // Đảm bảo đóng ResultSet
            }
        }
    }

    /**
     * Tạo một bảng trong cơ sở dữ liệu.
     *
     * @param createTableSQL Câu lệnh SQL để tạo bảng.
     * @return true nếu bảng được tạo thành công, false nếu không.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình thực thi câu lệnh.
     */
    public boolean createTable(String createTableSQL) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(createTableSQL) == 0; // Nếu không có lỗi, trả về true
        }
    }

    /**
     * Chèn một bản ghi vào bảng và trả về khóa chính được tạo.
     *
     * @param insertSQL Câu lệnh SQL để chèn bản ghi.
     * @param params    Tham số cho câu lệnh chèn.
     * @return Khóa chính được tạo, hoặc null nếu không thành công.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình thực thi.
     */
    public Object insert(String insertSQL, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getObject(1); // Trả về khóa chính được tạo
            } else {
                return null; // Không có khóa chính được tạo
            }
        }
    }

    /**
     * Cập nhật một bản ghi trong bảng.
     *
     * @param updateSQL Câu lệnh SQL để cập nhật bản ghi.
     * @param params    Tham số cho câu lệnh cập nhật.
     * @return Số lượng hàng bị ảnh hưởng.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình thực thi.
     */
    public int update(String updateSQL, Object... params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement.executeUpdate(); // Trả về số lượng hàng bị ảnh hưởng
        }
    }

    /**
     * Truy vấn một bản ghi từ bảng dựa trên ID.
     *
     * @param querySQL Câu lệnh SQL để truy vấn bản ghi.
     * @param params   Tham số cho câu lệnh truy vấn.
     * @return Đối tượng JsonObject đại diện cho bản ghi.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình thực thi.
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
                return jsonObject; // Trả về đối tượng JsonObject
            }
        }
        return null; // Nếu không có bản ghi nào được tìm thấy
    }

    /**
     * Truy vấn tất cả các bản ghi từ bảng.
     *
     * @param querySQL Câu lệnh SQL để truy vấn.
     * @return Mảng JsonArray chứa tất cả các bản ghi.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình thực thi.
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
                jsonArray.add(jsonObject); // Thêm đối tượng vào JsonArray
            }
        }
        return jsonArray; // Trả về JsonArray chứa tất cả các bản ghi
    }

    /**
     * Đóng kết nối đến cơ sở dữ liệu.
     *
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình đóng kết nối.
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Đóng kết nối
        }
    }
}
