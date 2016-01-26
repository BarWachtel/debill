package api.controller;

import api.route.pojo.response.JsonResponse;
import database.dao.JDBCBillDAO;
import database.entity.Bill;

import java.util.List;

public class BillController {
	public static JsonResponse getAll() {
		List<Bill> bills = JDBCBillDAO.getInstance().getAllBills();

		JsonResponse jsonResponse = JsonResponse.ok(bills);
		return jsonResponse;
	}

	public static JsonResponse get(int id) {
		Bill bill = JDBCBillDAO.getInstance().getBill(id);

		JsonResponse jsonResponse = JsonResponse.ok(bill);
		return jsonResponse;
	}

	public static JsonResponse insertBill(Bill i_bill) {
		Bill bill = JDBCBillDAO.getInstance().insertBill(i_bill);

		JsonResponse jsonResponse = JsonResponse.okIfLegalId(bill);
		return jsonResponse;
	}

	public static JsonResponse updateBill(Bill i_bill) {
		Bill bill = JDBCBillDAO.getInstance().updateBill(i_bill);

		JsonResponse jsonResponse = JsonResponse.ok(bill);
		return jsonResponse;
	}
}


