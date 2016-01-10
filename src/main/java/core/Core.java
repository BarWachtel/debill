package core;

import api.service.SessionService;
import core.ocr.OcrClient;
import core.parse.ParsedBillItem;
import core.parse.BillTextParser;
import database.dao.JDBCBillDAO;
import database.entity.Bill;
import database.entity.Item;
import database.entity.User;
import generalutils.FileUtils;
import redis.RedisClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Core {

	public Bill createNewBill(File billImageFile) {
		List<ParsedBillItem> parsedBillItems = imgToBillItems(billImageFile);
		Bill bill = new Bill();
		int userId = SessionService.getUserIdFromStoredSession();
		User user = new User(); // Should get the user from DB

		user.setID(userId);
		bill.setManager(user);

		List<Item> items = parsedItemsToDatabaseItems(parsedBillItems);
		bill.addItems(items);
		bill.setIsPrivate(false); // Default
		Bill insertedBill = JDBCBillDAO.getInstance().insertBill(bill);
		
		if (insertedBill.getID() >= 0) {
			bill.setID(insertedBill.getID());
			RedisClient.getInstance().setBillByUserId(bill);
			RedisClient.getInstance().setItemsOfBill(insertedBill);
		}

		return insertedBill;
	}

	public boolean updateBill(int userId, List<Item> updatedItems) {
		Bill bill = getBill(userId);
		boolean success = false;
		if ((success = bill.update(updatedItems))) {
			RedisClient.getInstance().removeAllItems(bill);
			RedisClient.getInstance().setItemsOfBill(bill);
			JDBCBillDAO.getInstance().updateBill(bill);
		}

		return success;
	}

	private Bill getBill(int userId) {
		Bill bill = RedisClient.getInstance().getBillByUserId(userId);
		if (bill == null) {
			bill = JDBCBillDAO.getInstance().getOpenBillByUserId(userId);
		}

		return bill;
	}

	private List<Item> parsedItemsToDatabaseItems(List<ParsedBillItem> parsedBillItems) {
		List<Item> items = new ArrayList<>(parsedBillItems.size());

		for (ParsedBillItem parsedBillItem : parsedBillItems) {
			items.add(new Item(parsedBillItem));
		}

		return items;
	}

	private List<ParsedBillItem> imgToBillItems(File billImageFile) {
		String imgFileName = FileUtils.saveBinaryFile(billImageFile);

		List<String> billLines = OcrClient.getImgLines(imgFileName);

		BillTextParser billParser = new BillTextParser();
		List<ParsedBillItem> parsedBillItems = billParser.parse(billLines);

		return parsedBillItems;
	}

	public static void main(String[] args) {
		String imgName = "img/bill_crop.png";
		File imgFile = FileUtils.loadFile(imgName);

		Core core = new Core();
		core.imgToBillItems(imgFile);
	}
}
