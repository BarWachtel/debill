package database.entity;

import java.util.ArrayList;
import java.util.List;

public class BillSummary extends Entity {
    private Bill bill;
    private final List<ItemSummary> itemSummaryList;

    public BillSummary() {
        itemSummaryList = new ArrayList<>();
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void addItemSummary(ItemSummary itemSummary) {
        this.itemSummaryList.add(itemSummary);
    }

    public void removeItemSummary(ItemSummary itemSummary) {
        for (int i = 0; i < this.itemSummaryList.size(); i++) {
            if (this.itemSummaryList.get(i).equals(itemSummary)) {
                this.itemSummaryList.remove(i);
                break;
            }
        }
    }
}
