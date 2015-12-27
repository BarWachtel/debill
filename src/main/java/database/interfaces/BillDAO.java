package database.interfaces;

import database.entity.Bill;

import java.util.List;

public interface BillDAO {
    boolean updateBill(Bill bill);
    boolean deleteBill(Bill bill);
	int insertBill(Bill bill);
	Bill getOpenBillByUserId(int userId);
}
