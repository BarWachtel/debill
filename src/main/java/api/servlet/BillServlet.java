package api.servlet;

import api.controller.BillController;
import api.utils.ResponseUtil;
import database.dao.BillDAO;
import database.entity.Bill;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

@WebServlet(name = "BillApiServlet", urlPatterns = {"/bill/*"})
public class BillServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Post request: " + request.getRequestURL().toString());

		ResponseUtil.send("You are unauthorized", response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Get request: " + request.getRequestURL().toString());
		for(Map.Entry<String, String[]> paramEntry : request.getParameterMap().entrySet()) {
			System.out.println(paramEntry.getKey() + ": " + paramEntry.getValue()[0]);
		}

		JSONArray allBills = BillController.getAll();

		ResponseUtil.send("You are unauthorized", response);
	}
}
