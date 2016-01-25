package api.route;

import api.controller.LoginController;
import api.route.pojo.request.UserLoginDetails;
import api.route.pojo.response.JsonResponse;
import api.service.SessionService;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by user on 03/01/2016.
 */
@Path("/login")
public class LoginRoute {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JsonResponse userLogin(UserLoginDetails userLoginDetails, @Context HttpServletRequest request) {
		SessionService.addSessionToLocalStore(request);
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setSuccess(LoginController.login(userLoginDetails));
		return jsonResponse;
	}

    @GET
    public String getUserId(@Context HttpServletRequest request) {
		return String.valueOf(SessionService.getUserIdFromRequest(request));
//        return Response.status(200).entity(SessionService.getUserIdFromRequest(request)).build();
    }
}
