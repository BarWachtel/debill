package api.route;

import api.controller.BillController;
import api.controller.SummaryController;
import database.entity.Bill;
import database.entity.BillSummary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Dima on 10/01/2016.
 */
@Path("/summary")
public class SummaryRoute {
    @GET @Path("/{biilId}") @Produces(MediaType.APPLICATION_JSON)
    public BillSummary getSummaryByBill(@PathParam("id" ) int biilId){
        BillSummary billSummary = SummaryController.getSummaryByBill(biilId);
        return billSummary;
    }

    @GET @Path("/{id}/{userId}") @Produces(MediaType.APPLICATION_JSON)
    public BillSummary getSummaryByBillIdAndUserId(@PathParam("id" ) int biilId,@PathParam("userId" ) int userId)
    {
        BillSummary billSummary = SummaryController.getSummaryByBillIDAndUserId(biilId,userId);
        return billSummary;
    }

}
