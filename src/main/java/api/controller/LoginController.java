package api.controller;

import api.route.pojo.request.UserLoginDetails;
import api.route.pojo.response.JsonResponse;
import api.service.SessionService;
import database.entity.User;

/**
 * Created by user on 10/01/2016.
 */
public class LoginController {
	public static JsonResponse login(UserLoginDetails userLoginDetails) {
		JsonResponse jsonResponse = null;
		String errorMsg = null;

		User user = UserController.getUser(userLoginDetails.getUsername());
		if (user != null) {
			if (user.comparePassword(userLoginDetails.getPassword())) {
				if (user.hasLegalId()) {
					jsonResponse = JsonResponse.ok(user);
					SessionService.setUserId(user.getID());
				} else {
					errorMsg = "Unknown error occured";
				}
			} else {
				errorMsg = "Password incorrect";
			}
		} else {
			errorMsg = "Username does not exist";
		}

		if (jsonResponse == null) {
			jsonResponse = JsonResponse.error(errorMsg);
		}

		return jsonResponse;
	}
}
