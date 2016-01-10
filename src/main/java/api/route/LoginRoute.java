package api.route;

import api.service.SessionFilter;
import api.service.SessionService;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by user on 03/01/2016.
 */
@Path("/login")
public class LoginRoute {
    @POST
    public void userLogin(@Context HttpServletRequest request, @PathParam("user_id") int userId) {
        SessionService.addSessionToLocalStore(request);
        SessionService.setUserId(userId);
        String header = request.getHeader("authorization");
        String data = header.substring(header.indexOf("") + 1);
        byte[] bytes = null;
        try {
            bytes = new BASE64Decoder().decodeBuffer(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String decoded = new String(bytes);
        StringTokenizer tokenizer = new StringTokenizer(decoded, ":");

    }

    @GET
    public Response getUserId() {
        return Response.status(200).entity(SessionService.getUserId()).build();
    }
}
