package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {
    public static void main(String[] args) {

        UserServiceImpl userDao = new UserServiceImpl();

        userDao.createUsersTable();

        userDao.saveUser("Jeka", "LastName1", (byte) 21);
        userDao.saveUser("Name2", "LastName2", (byte) 25);
        userDao.saveUser("Name3", "LastName3", (byte) 31);
        userDao.saveUser("Name4", "LastName4", (byte) 38);

        userDao.getAllUsers();

        userDao.cleanUsersTable();

        userDao.dropUsersTable();

    }
}
