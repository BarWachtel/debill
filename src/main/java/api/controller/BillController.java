package api.controller;

import database.dao.JDBCBillDAO;
import database.entity.Bill;

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

	public static Bill insertBill(Bill i_bill) {
		Bill bill = JDBCBillDAO.getInstance().insertBill(i_bill);
		return bill;
	}

	public static Bill updateBill(Bill i_bill) {
		Bill bill = JDBCBillDAO.getInstance().updateBill(i_bill);
		return bill;
	}
}


