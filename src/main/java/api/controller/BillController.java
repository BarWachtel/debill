package api.controller;

import database.dao.BillDAO;
import database.entity.Bill;
import org.json.JSONArray;

import java.util.Collection;
import java.util.List;

/**
 * Created by user on 01/12/2015.
 */
public class BillController {
	public static List<Bill> getAll() {
		List<Bill> bills = BillDAO.getAll();
		return bills;
	}

	public static Bill get(int id) {
		Bill bill = BillDAO.get(id);
		return bill;
	}
}


