package database.entity;

import java.util.ArrayList;
import java.util.List;

public class Bill extends Entity {

    private User manager;
    private boolean isPrivate;
    private boolean isOpen;
    private List<Item> items;

    public Bill() {
		items = new ArrayList<>();
    }

    public Bill(int id, User manager, boolean isPrivate, boolean isOpen, List<Item> items) {
        super(id);
        this.manager = manager;
        this.isPrivate = isPrivate;
        this.isOpen = isOpen;
		if (items == null) {
			items = new ArrayList<>();
		}
        this.items = items;
	}

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItems(List<Item> items) {
		if (items != null) {
			this.items.addAll(items);
		}
    }

    @Override
    public String toString() {
        return "Bill id: " + id + " getIsPrivate: " + isPrivate + " getIsOpen: " + isOpen;
    }

    public boolean update(List<Item> updatedItems) {
        boolean success = true;
        for (Item updatedItem : updatedItems) {
            Item toUpdate = getItem(updatedItem.getID());
            if (!(success = toUpdate.update(updatedItem))) {
                break;
            }
        }

        return success;
    }

    private Item getItem(int id) {
        Item item = null;
        for (Item currentItem : items) {
            if (currentItem.getID() == id) {
                item = currentItem;
                break;
            }
        }

        return item;
    }

    @Override
    public int getID() {
        return id;
    }
}

/*
CREATE TABLE `dbName`.`bill` (
  `id` INT NOT NULL,
  `manager` INT NULL,
  `private` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));
 */