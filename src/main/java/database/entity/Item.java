package database.entity;

import core.parse.ParsedBillItem;

public class Item extends Entity {

	private String name = null;
	private int quantity = -1;
    private float price = -1;
	private int billId = -1;
	private int numPayedFor = 0;

    public Item() {

    }

    public Item(int id, int quantity, float price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

	public Item(ParsedBillItem parsedBillItem) {
		this.name = parsedBillItem.getName();
		this.price = parsedBillItem.getPrice();
		this.quantity = parsedBillItem.getQuantity();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

	public boolean update(Item updatedItem) {
		if (updatedItem.name != null) {
			this.name = updatedItem.name;
		}

		if (updatedItem.price >= 0) {
			this.price = updatedItem.price;
		}

		if (updatedItem.quantity >= 0) {
			this.quantity = updatedItem.quantity;
		}

		this.numPayedFor = updatedItem.numPayedFor;

		// All updates are allowed!
		return true;
	}
}

/**
 CREATE TABLE `debill`.`category` (
 `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 `name` VARCHAR(150) NOT NULL,
 `price` DECIMAL(10,2) NOT NULL,
 `quantity` INT NULL DEFAULT 1,
 `billid` INT NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX `id_UNIQUE` (`id` ASC));
 */