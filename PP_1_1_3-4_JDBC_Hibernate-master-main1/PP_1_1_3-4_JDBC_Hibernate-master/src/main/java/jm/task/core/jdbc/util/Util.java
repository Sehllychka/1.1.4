package jm.task.core.jdbc.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String name = "root";
    private static final String password = "root";

    private Util() {
    }

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, name, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return connection;
    }
}
