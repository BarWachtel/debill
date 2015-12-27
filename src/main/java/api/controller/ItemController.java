package api.controller;

import database.dao.JDBCItemDAO;
import database.entity.Bill;
import database.entity.Item;

import java.util.List;


public class ItemController {

    public static Item getItem(Bill i_bill,int id) {
       Item item = JDBCItemDAO.getItem(i_bill, id);
       return item;
    }

    public static List<Item> getAllBillItems(Bill i_bill) {
        List<Item> items = JDBCItemDAO.getAllItems(i_bill);
        return items;
    }

}
