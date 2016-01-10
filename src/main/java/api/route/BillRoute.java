package api.route;

import api.controller.BillController;
import api.controller.ItemController;
import database.entity.Bill;
import database.entity.Item;
import generalutils.thread.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/bill") public class BillRoute {
	@GET @Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllBills() {
		List<Bill> bills = BillController.getAll();
		return bills;
	}

	@GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Bill getBill(@PathParam("id") int id) {
		Bill bill = BillController.get(id);
		return bill;
	}

	@POST @Path("/create")
	public void createBill(@Context HttpServletRequest request) {
		// I need this later - see Core.createNewBill(..)
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, request.getSession(false));
	}

	@GET @Path("/{billId}/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Item getItem(@PathParam("billId") int i_billId, @PathParam("id") int id) {
		Item item = ItemController.getItem(id);
		return item;
	}

	@GET @Path("/{billId}/items") @Produces(MediaType.APPLICATION_JSON)
	public List<Item> getAllBillItems(@PathParam("billId") int i_billId) {
		List<Item> items = ItemController.getAllBillItems(i_billId);
		return items;
	}

	//TODEL
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/check")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consumeJSON(Bill i_BillJason) {
		String output = i_BillJason.toString();
		return Response.status(200).entity(output).build();

	}

}
