package api.route;

import api.controller.BillController;
import api.controller.UserController;
import com.google.gson.Gson;
import database.entity.Bill;
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

import static java.lang.Math.toIntExact;


@Path("/user")
public class UserRoute {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        List<User> users = UserController.getAllUsers();
        return users;
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject consumeJSON(String  inputJsonObj) throws Exception{
        System.out.println("in addBillMAnager");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(inputJsonObj);
        User user;

        //JSONObject billManager = (JSONObject) jsonObject.get("User");
        int  managerId = toIntExact((Long) jsonObject.get("id"));
        String firstName = (String) jsonObject.get("firstName");
        String lastName = (String) jsonObject.get("lastName");
        String facebookId = (String) jsonObject.get("facebookId");
        user =  new User(managerId,firstName,lastName,facebookId);
        System.out.println(
                "managerId: " + managerId +
                        "firstName: " + firstName +
                        "lastName: " + lastName +
                        "facebookId: " + facebookId

        );

        User resultUser = UserController.insertUser(user);
        Gson gson = new Gson();
        String jsonRes = gson.toJson(resultUser);
        JSONParser jsonParser2 = new JSONParser();
        JSONObject jsonObjectRes = (JSONObject) jsonParser2.parse(jsonRes);
        return jsonObjectRes;
    }
}
