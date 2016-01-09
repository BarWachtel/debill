package database.entity;

public class ItemSummary extends Entity {

    private User user;
    private Item item;
    private int quantity;

    public ItemSummary(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemSummary other = (ItemSummary) obj;
        boolean userNotValid = (this.user == null) ? (other.user != null) : !(this.user.getID() == other.user.getID());
        if (userNotValid) {
            return false;
        }
        boolean itemNotValid = (this.item == null) ? (other.item != null) : !(this.item.getID() == other.item.getID());
        if (itemNotValid) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.user != null ? this.user.getID() : 0);
        hash = 53 * hash + (this.item != null ? this.item.getID() : 0);
        hash = 53 * hash + this.quantity;
        return hash;

    }
}
