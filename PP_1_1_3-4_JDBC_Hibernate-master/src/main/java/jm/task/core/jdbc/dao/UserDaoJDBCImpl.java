package jm.task.core.jdbc.dao;

import com.mysql.cj.log.Log;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    Logger logger = Logger.getLogger(Log.LOGGER_INSTANCE_NAME);

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        String create = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) , last_Name VARCHAR(255), age TINYINT)";
        try (Connection connection = new Util().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(create);
            logger.info("The table has been created");
            try {
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users";
        try (Connection connection = new Util().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(drop);
            logger.info("The table has been deleted");
            try {
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String save = "INSERT INTO users (name, last_Name, age) VALUES (?, ?, ?)";

        try (Connection connection = new Util().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement
                     (save)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            logger.info("User %s was added".formatted(name));
            try {
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?";

        try (Connection connection = new Util().getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement
                    (remove)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                logger.info("User %s was added%n".formatted(id));
                try {
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String allUsers = "SELECT id, Name, last_Name, Age FROM users";

        try (Connection connection = new Util().getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(allUsers);

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_Name");
                byte age = resultSet.getByte("age");
                User user = new User(id, name, lastName, age);
                users.add(user);
                System.out.printf("user id=%d, name=%s, last name=%s, age=%d%n", user.getId(),user.getName(),user.getLastName(),user.getAge());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String clean = "TRUNCATE TABLE users";
        try (Connection connection = new Util().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(clean);
            try {
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}