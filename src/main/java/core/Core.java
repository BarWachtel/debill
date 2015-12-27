package core;

import core.ocr.OcrClient;
import core.parse.ParsedBillItem;
import core.parse.BillTextParser;
import database.dao.JDBCBillDAO;
import database.dao.JDBCUserDAO;
import database.entity.Bill;
import database.entity.Item;
import database.entity.User;
import generalutils.FileUtils;
import generalutils.thread.ThreadLocalUtil;
import redis.RedisClient;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/12/2015.
 */
public class Core {

	public int createNewBill(File billImageFile) {
		List<ParsedBillItem> parsedBillItems = imgToBillItems(billImageFile);

		HttpSession httpSession = (HttpSession) ThreadLocalUtil.get(ThreadLocalUtil.USER_SESSION);

		Bill bill = new Bill();

		int userId = (int) httpSession.getAttribute("userId");
		User user = new User();
		user.setId(userId);
		bill.setManager(user);

		List<Item> items = parsedItemsToDatabaseItems(parsedBillItems);
		bill.setItems(items);

		bill.setPrivate(false);

		int billId = JDBCBillDAO.getInstance().insertBill(bill);
		
		if (billId >= 0) {
			bill.setId(billId);
			RedisClient.setBillByUserId(userId, billId);
			RedisClient.setBillById(billId, bill);
		}

		return billId;
	}

	private Bill getBill(int billId) {
		Bill bill = RedisClient.getBillById(billId);
		if (bill == null) {
			bill = JDBCBillDAO.getInstance().getOpenBillByUserId(billId);
		}

		return bill;
	}

	public boolean updateBill(int billId, List<Item> updatedItems) {
		Bill bill = getBill(billId);
		boolean success = false;
		if ((success = bill.update(updatedItems))) {
			RedisClient.setBillById(billId, bill);
			JDBCBillDAO.getInstance().updateBill(bill);
		}

		return success;
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
