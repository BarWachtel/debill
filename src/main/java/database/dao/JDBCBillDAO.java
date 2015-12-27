package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.interfaces.BillDAO;
import database.interfaces.QueryBuilder.QueryBuilderException;

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
        fbId("fb_id"),
        isPrivate("private");

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
    public boolean updateBill(Bill bill) {
        return updateEntity(bill);
    }

    @Override
    public boolean deleteBill(Bill bill) {
        return deleteEntity(bill.getId());
    }

    @Override
    public int insertBill(Bill bill) {
        return insertBill(bill);
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
            newBill.setPrivate(rs.getBoolean(Columns.isPrivate.getAsString()));
            newBill.setManager(null);
            newBill.setItems(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newBill;
    }

    @Override
    protected Collection<String> getColumnsForUpdate() {
        List<String> cols = new ArrayList<>();
        cols.add(Columns.isPrivate.getAsString());
        return cols;
    }

    @Override
    protected String buildInsertQuery(Bill entity) {
        String query = null;
        try {
            query = queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .value(Columns.billId.getAsString() + "=" + entity.getId())
                    .value(Columns.isPrivate.getAsString() + "=" + entity.isPrivate())
                    .build();
        } catch (QueryBuilderException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Bill entity) {
        try {
            ps.setBoolean(1, entity.isPrivate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, Bill entity) {
        try {
            ps.setInt(1, entity.getId());
            ps.setBoolean(2, entity.isPrivate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(new JDBCBillDAO().getColumnsForUpdate());
    }
}