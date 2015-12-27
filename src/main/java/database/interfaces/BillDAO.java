package database.interfaces;

import database.entity.Bill;

import java.util.List;

public interface BillDAO {
    List<Bill> getAllBills();
    Bill getBill(int id);
    boolean updateBill(Bill bill);
    boolean deleteBill(Bill bill);
	int insertBill(Bill bill);
}
