package database.interfaces;

import database.entity.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();
    User getUser(int id);
	User getUser(String username);
    User updateUser(User user);
    boolean deleteUser(User user);
    User insertUser(User user);
}