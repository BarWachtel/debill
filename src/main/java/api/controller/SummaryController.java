package api.controller;


import database.dao.JDBCSummaryDAO;
import database.entity.Bill;
import database.entity.BillSummary;

public class SummaryController {

    public static BillSummary getSummaryByBill(int i_billId)
    {
        return JDBCSummaryDAO.getInstance().getSummaryByBillId(i_billId);
    }

    public static BillSummary getSummaryByBillIDAndUserId(int i_billId,int i_userId)
    {
        return JDBCSummaryDAO.getInstance().getSummaryByBillIdAndUserId(i_billId,i_userId);
    }
}
