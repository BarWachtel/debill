package api.controller;

import database.dao.JDBCItemDAO;
import database.entity.Bill;
import database.entity.Item;

import java.util.List;

public class ItemController {

	public static Item getItem(int item_id) {
		Item item = JDBCItemDAO.getInstance().getItem(item_id);
		return item;
	}

	public static List<Item> getAllBillItems(int bill_id) {
		List<Item> items = JDBCItemDAO.getInstance().getAllItems(bill_id);
		return items;
	}

}
