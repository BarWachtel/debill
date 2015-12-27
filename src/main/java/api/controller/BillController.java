package api.controller;

import database.dao.JDBCBillDAO;
import database.dao.JDBCItemDAO;
import database.dao.SampleDAO;
import database.entity.Bill;
import database.entity.Item;

import java.util.List;

public class BillController {
	public static List<Bill> getAll() {
		List<Bill> bills = JDBCBillDAO.getInstance().getAllBills();
		return bills;
	}

	public static Bill get(int id) {
		Bill bill = JDBCBillDAO.getInstance().getBill(id);
		return bill;
	}


}


