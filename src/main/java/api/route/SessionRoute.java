package api.route;

import api.controller.BillController;
import database.entity.Bill;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by user on 24/12/2015.
 */
@Path("/session")
public class SessionRoute {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSessionId(@Context HttpServletRequest request) {
		return request.getSession(true).getId();
	}

	@POST
	public void addParamToSession(@Context HttpServletRequest request,
			@QueryParam("param") String param) {
		request.getSession(true).setAttribute("sessionParam", param);
	}

	@GET
	@Path("/param")
	public String getParamFromSession(@Context HttpServletRequest request) {
		return (String) request.getSession(true).getAttribute("sessionParam");
	}
}
