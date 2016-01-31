package api.controller;

import api.route.pojo.response.JsonResponse;
import api.service.SessionService;
import database.dao.JDBCGroupDAO;
import database.dao.JDBCItemDAO;
import database.dao.JDBCUserDAO;
import database.entity.Item;
import database.entity.User;

import java.util.List;

public class UserController {

    public static JsonResponse getBillUsers(int bill_id) {
		JsonResponse jsonResponse;
		List<User> billUsers = JDBCGroupDAO.getInstance().getGroupUsersByBillId(bill_id).getUsers();
        jsonResponse = JsonResponse.ok(billUsers);
		return jsonResponse;
    }

    public static JsonResponse insertUser(User i_user) {
		JsonResponse jsonResponse;
		User user = JDBCUserDAO.getInstance().insertUser(i_user);
		jsonResponse = JsonResponse.okIfLegalId(user);
		if (!jsonResponse.isError()) {
			SessionService.setUserId(user.getID());
		}
		return jsonResponse;
	}

    public static JsonResponse getAllUsers() {
		JsonResponse jsonResponse;
		List<User> users = JDBCUserDAO.getInstance().getAllUsers();
		jsonResponse = JsonResponse.ok(users);
		return jsonResponse;
	}

	public static User getUser(int id) {
		return JDBCUserDAO.getInstance().getUser(id);
	}

	public static User getUser(String username) {
		return JDBCUserDAO.getInstance().getUser(username);
	}
}
