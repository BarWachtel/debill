package api.route;

import api.controller.UserController;
import api.route.pojo.response.JsonResponse;
import database.dao.JDBCUserDAO;
import database.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;



@Path("/user")
public class UserRoute {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonResponse getAllUsers() {
        List<User> users = UserController.getAllUsers();
        return JsonResponse.ok(users);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonResponse insertUser(User user) throws Exception {
        User userWithId = JDBCUserDAO.getInstance().insertUser(user);
		JsonResponse jsonResponse;
		if (userWithId.hasLegalId()) {
			jsonResponse = JsonResponse.ok(userWithId);
		} else {
			jsonResponse = JsonResponse.error("Failed to insert user");
		}
        return jsonResponse;
    }
}
