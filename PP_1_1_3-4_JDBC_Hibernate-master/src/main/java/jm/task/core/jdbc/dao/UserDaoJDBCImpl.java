package jm.task.core.jdbc.dao;

import com.mysql.cj.log.Log;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    Logger logger = Logger.getLogger(Log.LOGGER_INSTANCE_NAME);

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        final String create = "CREATE TABLE IF NOT EXISTS users" +
                " (id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                " name VARCHAR(255) ," +
                " last_Name VARCHAR(255)," +
                " age TINYINT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(create);
            logger.info("The table has been created");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(drop);
            logger.info("The table has been deleted");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String save = "INSERT INTO users (name, last_Name, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(save)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User %s was added%n", name);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.getStackTrace();
            }
            e.getStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(remove)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.getStackTrace();
            }
            e.getStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String allUsers = "SELECT id, Name, last_Name, Age FROM users";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(allUsers);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_Name");
                byte age = resultSet.getByte("age");
                User user = new User(id, name, lastName, age);
                users.add(user);
                connection.commit();
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String clean = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(clean);
            logger.info("The user table has been cleared");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }
}
