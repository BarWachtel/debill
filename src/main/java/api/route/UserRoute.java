package api.route;

import api.controller.UserController;
import com.google.gson.Gson;
import database.dao.JDBCUserDAO;

import database.entity.Item;
import database.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



@Path("/user")
public class UserRoute {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        List<User> users = UserController.getAllUsers();
        return users;
    }

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") int id) {
		return UserController.getUser(id);
	}

	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User insertUser(User user) throws Exception {
        User userWithId= JDBCUserDAO.getInstance().insertUser(user);
        return userWithId;
    }


}

//    @POST
//    @Path("/user")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public JSONObject consumeJSON(String  inputJsonObj) throws Exception{
//        System.out.println("in addBillMAnager");
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) jsonParser.parse(inputJsonObj);
//        User user;
//
//        //JSONObject billManager = (JSONObject) jsonObject.get("User");
//        int  managerId = toIntExact((Long) jsonObject.get("id"));
//        String firstName = (String) jsonObject.get("firstName");
//        String lastName = (String) jsonObject.get("lastName");
//        String facebookId = (String) jsonObject.get("facebookId");
//        user =  new User(managerId,firstName,lastName,facebookId);
//        System.out.println(
//                "managerId: " + managerId +
//                        "firstName: " + firstName +
//                        "lastName: " + lastName +
//                        "facebookId: " + facebookId
//
//        );
//
//        User resultUser = UserController.insertUser(user);
//        Gson gson = new Gson();
//        String jsonRes = gson.toJson(resultUser);
//        JSONParser jsonParser2 = new JSONParser();
//        JSONObject jsonObjectRes = (JSONObject) jsonParser2.parse(jsonRes);
//        return jsonObjectRes;
//    }


