package api.route;

import api.controller.BillController;
import api.controller.SummaryController;
import database.dao.JDBCSummaryDAO;
import database.dao.JDBCUserDAO;
import database.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public class SummaryRoute {
    @GET @Path("/bill/{id}/summary") @Produces(MediaType.APPLICATION_JSON)
    public BillSummary getSummaryByBill(@PathParam("id" ) int billId){
        BillSummary billSummary = SummaryController.getSummaryByBill(billId);
        return billSummary;
    }

    @GET @Path("/bill/{billId}/user/{userId}/{userId}") @Produces(MediaType.APPLICATION_JSON)
    public BillSummary getSummaryByBillIdAndUserId(@PathParam("billId") int billId,@PathParam("userId" ) int userId)
    {
        BillSummary billSummary = SummaryController.getSummaryByBillIDAndUserId(billId,userId);
        return billSummary;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ItemSummary consumeJSON(ItemSummary itemSummary) throws Exception {
        ItemSummary resItemSummary= JDBCSummaryDAO.getInstance().addItemToSummary(itemSummary);
        return resItemSummary;
    }
}
