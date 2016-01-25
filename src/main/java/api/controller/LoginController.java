package api.controller;

import api.route.pojo.request.UserLoginDetails;
import api.service.SessionService;
import database.entity.User;

/**
 * Created by user on 10/01/2016.
 */
public class LoginController {
	public static boolean login(UserLoginDetails userLoginDetails) {
		boolean success = false;
		User user = UserController.getUser(userLoginDetails.getUsername());
		if (user != null) {
			if (user.comparePassword(userLoginDetails.getPassword())) {
				int userId = user.getID();
				System.out.println(userId);
				if (userId >= 0) {
				SessionService.setUserId(userId);
					success = true;
				}
			}
		}

		return success;
	}
}
