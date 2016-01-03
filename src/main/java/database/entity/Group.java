package database.entity;

import java.util.List;

public class Group extends Entity {

    private List<User> users;
    private Bill bill;

    public void addUserToGroup(User user) {
        users.add(user);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
