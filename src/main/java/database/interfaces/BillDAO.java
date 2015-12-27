package database.interfaces;

import database.entity.Bill;

import java.util.List;

public interface BillDAO {
    List<Bill> getAllBills();
    Bill getBill(int id);
    Bill updateBill(Bill bill);
    boolean deleteBill(Bill bill);
	Bill insertBill(Bill bill);
	Bill getOpenBillByUserId(int userId);
}
