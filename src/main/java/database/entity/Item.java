package database.entity;

public class Item {

    private int id;
	private String name;
	private int quantity;
    private float price;
	private int billId;

    public Item() {

    }

    public Item(int id, int quantity, float price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
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