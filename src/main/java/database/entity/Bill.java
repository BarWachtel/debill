package database.entity;

import java.util.List;

public class Bill extends Entity {

    private int id;
    private User manager;
    private boolean isPrivate;
    private List<Item> items;

    public Bill() {

    }

    public Bill(int id, User manager, boolean isPrivate, List<Item> items) {
        this.id = id;
        this.manager = manager;
        this.isPrivate = isPrivate;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Bill id: ").append(id).append(" isPrivate: ").append(isPrivate);
		return sb.toString();
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