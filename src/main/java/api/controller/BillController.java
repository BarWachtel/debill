package api.controller;

import database.dao.JDBCBillDAO;
import database.dao.JDBCItemDAO;
import database.dao.SampleDAO;
import database.entity.Bill;
import database.entity.Item;

import java.util.List;

/**
 * Created by user on 01/12/2015.
 */
public class BillController {
	public static List<Bill> getAll() {
		List<Bill> bills = JDBCBillDAO.getAllBills();
		return bills;
	}

	public static Bill get(int id) {
		Bill bill = JDBCBillDAO.getBill(id);
		return bill;
	}


}


