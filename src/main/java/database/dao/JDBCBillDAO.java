package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.interfaces.BillDAO;
import database.interfaces.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCBillDAO extends SampleDAO<Bill> implements BillDAO {

    private static final JDBCBillDAO instance = new JDBCBillDAO();

    public static JDBCBillDAO getInstance() {
        return instance;
    }

    static {
        TABLE_NAME = "bills";
    }

    private JDBCBillDAO() {
    }

    private enum Columns {
        billId("bill_id"),
        userId("user_id"),
        isPrivate("private"),
        isOpen("open");

        private String asString;

        Columns(String asString) {
            this.asString = asString;
        }

        public String getAsString() {
            return asString;
        }
    }

    @Override
    public List<Bill> getAllBills() {
        return getAllEntities();
    }

    @Override
    public Bill getBill(int id) {
        return getEntity(id);
    }

    @Override
    public Bill updateBill(Bill bill) {
        return updateEntity(bill);
    }

    @Override
    public boolean deleteBill(Bill bill) {
        return deleteEntity(bill.getId());
    }

    @Override
    public Bill insertBill(Bill bill) {
        return insertEntity(bill);
    }

    @Override
    public Bill getOpenBillByUserId(int userId) {
        Connection conn = DBConn.getConnection();
        Bill openBillByUserId = null;
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .where(Columns.isOpen.getAsString() + "=?")
                    .where(Columns.userId.getAsString() + "=?")
                    .build();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                openBillByUserId = createEntityFromResultSet(rs);
            }
        } catch (SQLException | QueryBuilder.QueryBuilderException ee) {
            ee.printStackTrace();
        }
        return openBillByUserId;
    }

    @Override
    protected String getIdColumnName() {
        return Columns.billId.getAsString();
    }

    @Override
    protected Bill createEntityFromResultSet(ResultSet rs) {
        Bill newBill = new Bill();
        try {
            newBill.setId(rs.getInt(Columns.billId.getAsString()));
            newBill.setId(rs.getInt(Columns.userId.getAsString()));
            newBill.setPrivate(rs.getBoolean(Columns.isPrivate.getAsString()));
            newBill.setOpen(rs.getBoolean(Columns.isOpen.getAsString()));
            newBill.setManager(null);
            newBill.setItems(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newBill;
    }

    @Override
    protected Collection<String> getColumnsForUpdate() {
        ArrayList<String> colsForUpdate = new ArrayList<>();
        colsForUpdate.add(Columns.isPrivate.getAsString());
        colsForUpdate.add(Columns.isOpen.getAsString());
        return colsForUpdate;
    }

    @Override
    protected Collection<String> getColumnsForInsert() {
        ArrayList<String> colsForInsert = new ArrayList<>();
        colsForInsert.add(Columns.userId.getAsString());
        colsForInsert.add(Columns.isPrivate.getAsString());
        colsForInsert.add(Columns.isOpen.getAsString());
        return colsForInsert;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Bill entity) {
        try {
            ps.setBoolean(1, entity.isPrivate());
            ps.setBoolean(2, entity.isOpen());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * @param entity the new bill, should contain user with id.
     */
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, Bill entity) {
        if (entity.getManager() != null) {
            try {
                ps.setInt(1, entity.getManager().getId());
                ps.setBoolean(2, entity.isPrivate());
                ps.setBoolean(3, true); // Set the new bill isOpen value to True by default
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("JDBCBillDAO -> setInsertPreparedStatementParameters -> manager == null");
        }
    }

    public static void main(String[] args) {
        System.out.println(new JDBCBillDAO().getColumnsForUpdate());
    }
}