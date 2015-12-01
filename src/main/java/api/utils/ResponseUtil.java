package api.utils;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by user on 01/12/2015.
 */
public class ResponseUtil {
	public static void send(String msg, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		doSend(msg.toString(), response);
	}

	public static void send(JSONObject msg, HttpServletResponse response) throws IOException {
		response.setContentType("text/json");
		doSend(msg.toString(), response);
	}

	private static void doSend(String msg, HttpServletResponse response) throws IOException {
		PrintWriter resp = response.getWriter();
		resp.print(msg);
		resp.flush();
	}
}
