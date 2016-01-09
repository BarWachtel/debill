package database.interfaces;

import database.entity.Bill;
import database.entity.Item;

import java.util.List;

public interface ItemDAO {
    List<Item> getAllItems(int billId);
    Item getItem(int itemId);
    Item updateItem(Item item);
    boolean deleteItem(Item item);
    Item insertItem(Item item);
}
