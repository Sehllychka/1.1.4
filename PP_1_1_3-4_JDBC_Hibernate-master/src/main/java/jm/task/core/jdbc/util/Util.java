package jm.task.core.jdbc.util;

import com.mysql.cj.jdbc.NonRegisteringDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/test";
    private final String name = "root";
    private final String password = "root";

    public Connection getConnection() {
        try {
            Driver driver = new NonRegisteringDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, name, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return connection;
    }
}
