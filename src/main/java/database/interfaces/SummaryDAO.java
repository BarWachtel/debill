package database.interfaces;

import database.entity.ItemSummary;
import database.entity.Summary;

public interface SummaryDAO {
    Summary getSummaryByBillId(int billId);
    Summary getSummaryByBillIdAndUserId(int billId, int userId);
    ItemSummary addItemToSummary(ItemSummary itemSummary);
    boolean deleteItemFromSummary(ItemSummary itemSummary);

}
