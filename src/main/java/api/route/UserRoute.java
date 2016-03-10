package api.route;

import api.controller.UserController;
import api.route.pojo.response.JsonResponse;
import api.service.SessionService;
import database.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserRoute {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonResponse getAllUsers() {
        return UserController.getAllUsers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonResponse insertUser(User user, @Context HttpServletRequest request) throws Exception {
		SessionService.addSessionToLocalStore(request);
		return UserController.insertUser(user);
    }
}
