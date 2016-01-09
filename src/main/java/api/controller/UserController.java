package api.controller;

import database.dao.JDBCGroupDAO;
import database.dao.JDBCItemDAO;
import database.dao.JDBCUserDAO;
import database.entity.Item;
import database.entity.User;

import java.util.List;


public class UserController {

    public static List<User> getBillUsers(int bill_id) {
        List<User> billUsers;
        billUsers = JDBCGroupDAO.getInstance().getGroupUsersByBillId(bill_id).getUsers();
        return billUsers;
    }

    public static User insertUser(User i_user) {
        User user = JDBCUserDAO.getInstance().insertUser(i_user);
        return user;
    }

    public static List<User> getAllUsers() {
        List<User> users = JDBCUserDAO.getInstance().getAllUsers();
        return users;
    }
}
