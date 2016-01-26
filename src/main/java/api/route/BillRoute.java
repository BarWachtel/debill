package api.route;

import api.controller.BillController;
import api.controller.ItemController;
import api.controller.UserController;
import database.entity.Bill;
import database.entity.Item;
import database.entity.User;
import generalutils.thread.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bill") public class BillRoute {
	//region Bill routes

	@GET @Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllBills() {
		List<Bill> bills = BillController.getAll();
		return bills;
	}

	@POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public Bill addBill(Bill i_bill) {
		return BillController.insertBill(i_bill);
	}

	@PUT @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public Bill updateBill(Bill i_bill) {
		return BillController.updateBill(i_bill);
	}

	@GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Bill getBill(@PathParam("id") int id) {
		System.out.println("BillRoute -> getBill with id " + id);
		Bill bill = BillController.get(id);
		return bill;
	}

	//endregion

	//region Item routes

	@GET @Path("/{billId}/item/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Item getItem(@PathParam("billId") int i_billId, @PathParam("id") int id) {
		Item item = ItemController.getItem(id);
		return item;
	}

	@GET @Path("/{billId}/items") @Produces(MediaType.APPLICATION_JSON)
	public List<Item> getAllBillItems(@PathParam("billId") int i_billId) {
		List<Item> items = ItemController.getAllBillItems(i_billId);
		return items;
	}

	@PUT @Path("/{billId}/item/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Item updateItem(Item item) {
		Item updatedItem = ItemController.updateItem(item);
		return updatedItem;
	}

	//endregion

	//region Bill helper routes
	@GET @Path("/{billId}/users") @Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllBillUsers(@PathParam("billId") int i_billId) {
		List<User> users = UserController.getBillUsers(i_billId);
		return users;
	}
	//endregion
}
