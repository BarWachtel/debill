package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by user on 01/12/2015.
 */
@Path("/test2")
public class Test {
	@GET
	public String test() {
		return "Its NOT WORKING :[";
	}
}
