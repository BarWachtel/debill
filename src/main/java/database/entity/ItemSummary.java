package database.entity;

public class ItemSummary extends Entity {

    private User user;
    private Item item;
    private int paidForQuantityByUser;

    public ItemSummary(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.paidForQuantityByUser = quantity;
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

    public int getPaidForQuantityByUser() {
        return paidForQuantityByUser;
    }

    public void setPaidForQuantityByUser(int paidForQuantityByUser) {
        this.paidForQuantityByUser = paidForQuantityByUser;
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
        if (this.paidForQuantityByUser != other.paidForQuantityByUser) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.user != null ? this.user.getID() : 0);
        hash = 53 * hash + (this.item != null ? this.item.getID() : 0);
        hash = 53 * hash + this.paidForQuantityByUser;
        return hash;

    }
}
