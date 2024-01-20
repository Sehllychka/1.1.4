package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String create = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255) ," +
                " last_Name VARCHAR(255)," +
                " age TINYINT)";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery(create);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    @Override
    public void dropUsersTable() {
        String drop = "DROP TABLE IF EXISTS users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createNativeQuery(drop);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            transaction.commit();
            System.out.printf("User %s was added%n", name);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            list = session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return list;
    }
    @Override
    public void cleanUsersTable() {
        String clean = "DELETE from User";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery(clean).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}