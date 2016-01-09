package database.interfaces;

import database.entity.Group;
import database.entity.User;

public interface GroupDAO {
    Group getGroupUsersByBillId(int billId);
    Group addUserToGroup(int billId, int userId);
    Group deleteUserFromGroup(int billId, int userId);
    boolean deleteGroupByBillId(int billId);
}
