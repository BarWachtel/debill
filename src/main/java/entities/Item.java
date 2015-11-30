package entities;

public class Item {

    private int id;
    private int amount;
    private float pricePerUnit;

    public Item() {

    }

    public Item(int id, int amount, float pricePerUnit) {
        this.id = id;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}