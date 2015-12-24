package api.route;

import api.controller.BillController;
import database.entity.Bill;
import generalutils.thread.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by user on 03/12/2015.
 */
@Path("/bill")
public class BillRoute {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllBills() {
		List<Bill> bills = BillController.getAll();
		return bills;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Bill getBill(@PathParam("id") int id) {
		Bill bill = BillController.get(id);
		return bill;
	}

	@POST
	@Path("/create")
	public void createBill(@Context HttpServletRequest request) {
		// I need this later - see Core.handleNewBill(..)
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, request.getSession(false));
	}
}
