package chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ChessConnectionManager implements ConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private Connection getConnection() {
        loadDriver();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T executeQuery(ConnectionMapper<T> connectionMapper) {
        try (final Connection connection = getConnection()) {
            return connectionMapper.execute(connection);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
