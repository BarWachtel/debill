package api.route;

import api.controller.BillController;
import api.controller.ItemController;
import api.controller.UserController;
import api.route.pojo.response.JsonResponse;
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
	public JsonResponse getAllBills() {
		return BillController.getAll();
	}

	@POST @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse addBill(Bill i_bill) {
		return BillController.insertBill(i_bill);
	}

	@PUT @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse updateBill(Bill i_bill) {
		return BillController.updateBill(i_bill);
	}

	@GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse getBill(@PathParam("id") int id) {
		return BillController.get(id);
	}

	//endregion

	//region Item routes

	@GET @Path("/{billId}/item/{id}") @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse getItem(@PathParam("billId") int i_billId, @PathParam("id") int id) {
		return ItemController.getItem(id);
	}

	@GET @Path("/{billId}/items") @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse getAllBillItems(@PathParam("billId") int i_billId) {
		return ItemController.getAllBillItems(i_billId);
	}

	@PUT @Path("/{billId}/item/{id}") @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse updateItem(Item item) {
		return ItemController.updateItem(item);
	}

	//endregion

	//region Bill helper routes
	@GET @Path("/{billId}/users") @Produces(MediaType.APPLICATION_JSON)
	public JsonResponse getAllBillUsers(@PathParam("billId") int i_billId) {
		return UserController.getBillUsers(i_billId);
	}
	//endregion
}
