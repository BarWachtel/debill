package api.route;

import api.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by user on 03/01/2016.
 */
@Path("/login")
public class LoginRoute {
	@POST
	public void userLogin(@Context HttpServletRequest request, @PathParam("user_id") int userId) {
		SessionService.addSessionToLocalStore(request);
		SessionService.setUserId(userId);
	}

	@GET
	public Response getUserId() {
		return Response.status(200).entity(SessionService.getUserId()).build();
	}
}
