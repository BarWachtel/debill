package api.controller;

import database.dao.BillDAO;
import database.entity.Bill;
import org.json.JSONArray;

import java.util.Collection;

/**
 * Created by user on 01/12/2015.
 */
public class BillController {
	public static JSONArray getAll() {
		Collection<Bill> theBills = BillDAO.getAll();
		JSONArray billsAsJsonArray = new JSONArray(theBills);
		return billsAsJsonArray;
	}
}
