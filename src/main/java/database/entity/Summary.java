package database.entity;

import java.util.ArrayList;
import java.util.List;

public class Summary extends Entity {
    private Bill bill;
    private int quantity;
    private final List<ItemSummary> itemSummaryList;

    public Summary() {
        itemSummaryList = new ArrayList<>();
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
