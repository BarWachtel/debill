package api.route;

import api.controller.BillController;
import database.entity.Bill;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
}
