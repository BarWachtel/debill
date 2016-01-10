package database.interfaces;

import database.entity.ItemSummary;
import database.entity.BillSummary;

public interface SummaryDAO {
    BillSummary getSummaryByBillId(int billId);
    BillSummary getSummaryByBillIdAndUserId(int billId, int userId);
    ItemSummary addItemToSummary(ItemSummary itemSummary);
    boolean deleteItemFromSummary(ItemSummary itemSummary);

}
