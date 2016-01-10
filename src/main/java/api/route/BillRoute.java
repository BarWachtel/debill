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
	@GET @Produces(MediaType.APPLICATION_JSON)
	public List<Bill> getAllBills() {
		List<Bill> bills = BillController.getAll();
		return bills;
	}

	@GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
	public Bill getBill(@PathParam("id") int id) {
		System.out.println("BillRoute -> getBill with id " + id);
		Bill bill = BillController.get(id);
		return bill;
	}

	@POST @Path("/create")
	public void createBill(@Context HttpServletRequest request) {
		// I need this later - see Core.createNewBill(..)
		ThreadLocalUtil.set(ThreadLocalUtil.USER_SESSION, request.getSession(false));
	}

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

	@GET @Path("/{billId}/users") @Produces(MediaType.APPLICATION_JSON)
	public List<User> getAllBillUsers(@PathParam("billId") int i_billId) {
		List<User> users = UserController.getBillUsers(i_billId);
		return users;
	}


	@POST @Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bill addBill(Bill i_bill) {
		return BillController.insertBill(i_bill);
	}

   @POST @Path("/update")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Bill updateBill(Bill i_bill) {
	   return BillController.updateBill(i_bill);
   }


	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Bill insertBill(Bill bill) throws Exception {
		return BillController.insertBill(bill);
	}
}

/**
 *
 System.out.println("in addNewBill");

 JSONParser jsonParser = new JSONParser();
 JSONObject jsonObject = (JSONObject) jsonParser.parse(inputJsonObj);
 Bill  bill;
 List<Item> billItems = new ArrayList<Item>();
 User userManager;
 Bill resultBill;
 int billID = toIntExact((Long) jsonObject.get("id"));
 System.out.println("bill id is: " + billID);

 JSONObject billManager = (JSONObject) jsonObject.get("User");
 int  managerId = toIntExact((Long) billManager.get("id"));
 String firstName = (String) billManager.get("firstName");
 String lastName = (String) billManager.get("lastName");
 String facebookId = (String) billManager.get("facebookId");
 userManager =  new User(managerId,firstName,lastName,facebookId);
 System.out.println(
 "managerId: " + managerId +
 "firstName: " + firstName +
 "lastName: " + lastName +
 "facebookId: " + facebookId

 );
 JSONObject creationTime =  (JSONObject) billManager.get("creationTime");
 int day = toIntExact((Long) creationTime.get("day"));
 int month = toIntExact((Long) creationTime.get("month"));
 int year = toIntExact((Long) creationTime.get("year"));


 System.out.println(
 "creationTime: " +
 "day: " + day +
 "month: " + month +
 "year: " + year

 );
 boolean getIsPrivate = Boolean.parseBoolean((String) jsonObject.get("getIsPrivate"));
 boolean getIsOpen = Boolean.parseBoolean((String) jsonObject.get("getIsOpen"));
 System.out.println(
 "bill booleans: " +
 "getIsPrivate: " + getIsPrivate +
 "getIsOpen: " + getIsOpen

 );
 JSONArray items = (JSONArray) jsonObject.get("items");
 Iterator i = items.iterator();
 while (i.hasNext()) {
 JSONObject innerObj = (JSONObject) i.next();
 String itemName = (String)innerObj.get("name");
 int quantity = toIntExact((Long) innerObj.get("quantity"));
 float price = (float)((Long)innerObj.get("price"));
 int billId = toIntExact((Long)innerObj.get("billId"));
 int numPaidFor = toIntExact((Long)innerObj.get("numPaidFor"));
 Item item =  new Item(itemName,quantity,price,billId,numPaidFor);
 billItems.add(item);
 System.out.println(
 "name: "+ itemName +
 "quantity: " + quantity +
 "price: " + price +
 "billId: " + billId +
 "numPaidFor: " + numPaidFor
 );

 }

 bill = new Bill(-1,userManager,getIsPrivate,getIsOpen,billItems);
 resultBill = BillController.insertBill(bill);
 Gson gson = new Gson();
 String jsonRes = gson.toJson(resultBill);
 JSONParser jsonParser2 = new JSONParser();
 JSONObject jsonObjectRes = (JSONObject) jsonParser2.parse(jsonRes);
 return jsonObjectRes;
 */
