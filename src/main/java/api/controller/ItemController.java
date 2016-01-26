package api.controller;

import api.route.pojo.response.JsonResponse;
import database.dao.JDBCItemDAO;
import database.entity.Bill;
import database.entity.Item;

import java.util.List;

public class ItemController {

	public static JsonResponse getItem(int item_id) {
		JsonResponse jsonResponse;
		Item item = JDBCItemDAO.getInstance().getItem(item_id);
		jsonResponse = JsonResponse.ok(item);
		return jsonResponse;
	}

	public static JsonResponse getAllBillItems(int bill_id) {
		JsonResponse jsonResponse;
		List<Item> items = JDBCItemDAO.getInstance().getAllItems(bill_id);
		jsonResponse = JsonResponse.ok(items);
		return jsonResponse;
	}

	public static JsonResponse updateItem(Item item) {
		JsonResponse jsonResponse;
		Item updatedItem = JDBCItemDAO.getInstance().updateItem(item);
		jsonResponse = JsonResponse.ok(updatedItem);
		return jsonResponse;
	}
}
