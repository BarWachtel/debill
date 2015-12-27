package database.interfaces;

import database.entity.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();
    User getUser(int id);
    User updateUser(User user);
    boolean deleteUser(User user);
    User insertUser(User user);
}