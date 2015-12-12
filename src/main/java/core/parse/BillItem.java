package core.parse;

/**
 * Created by user on 06/12/2015.
 */
public class BillItem {
	private int quantity;
	private String name;
	private float price;
	private float total;

	public BillItem() {
	}

	@Override public String toString() {
		return "BillItem{" +
				"quantity=" + quantity +
				", name='" + name + '\'' +
				", price=" + price +
				", total=" + total +
				'}';
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
}
