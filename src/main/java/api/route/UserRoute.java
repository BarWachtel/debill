package api.route;

import api.controller.UserController;
import database.dao.JDBCUserDAO;
import database.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
