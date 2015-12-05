package database.interfaces;

import database.entity.Bill;
import database.entity.Item;

import java.util.List;

public interface ItemDAO {
    List<Item> getAllItems(Bill bill);
    Item getItem(Bill bill, int itemId);
    boolean updateItem(Item item);
    boolean deleteItem(Item item);
}
