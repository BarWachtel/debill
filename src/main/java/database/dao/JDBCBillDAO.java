package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.interfaces.BillDAO;
import database.interfaces.QueryBuilder.QueryBuilderException;
import database.querybuilder.QueryBuilderFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCBillDAO implements BillDAO {

    private enum Columns {
        billId,
        fbId,
        isPrivate
    }

    private static final String tableName = "bills";
    private static final Map<Columns, String> columnsAsStrings = new HashMap<>();
    private static final QueryBuilderFactory queryBuilderFactory = new QueryBuilderFactory();

    static {
        columnsAsStrings.put(Columns.billId, "bill_id");
        columnsAsStrings.put(Columns.fbId, "fb_id");
        columnsAsStrings.put(Columns.isPrivate, "private");
    }

    @Override
    public List<Bill> getAllBills() {
        Connection conn = DBConn.getConnection();
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(tableName)
                    .build();
            ResultSet resultSet = conn.createStatement().executeQuery(query);
            List<Bill> bills = new ArrayList<>();
            while (resultSet.next()) {
                bills.add(createBillObject(resultSet));
            }
            conn.close();
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bill getBill(int id) {
        Connection conn = DBConn.getConnection();
        Bill bill = null;
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(tableName)
                    .where(columnsAsStrings.get(Columns.billId) + "=" + id)
                    .build();
            ResultSet resultSet = conn.createStatement().executeQuery(query);
            if (resultSet.next()) {
                bill = createBillObject(resultSet);
            }
            conn.close();
        } catch (QueryBuilderException | SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    @Override
    public boolean updateBill(Bill bill) {
        Connection conn = DBConn.getConnection();
        String query;
        int resultSet = 0;
        try {
            query = queryBuilderFactory
                    .update()
                    .from(tableName)
                    .set(columnsAsStrings.get(Columns.isPrivate) + " = ?")
                    .where(columnsAsStrings.get(Columns.billId) + " = ?")
                    .build();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1,bill.isPrivate());
            preparedStatement.setInt(2,bill.getId());
            resultSet = preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return resultSet == 1;
    }

    @Override
    public boolean deleteBill(Bill bill) {
        Connection conn = DBConn.getConnection();
        String query;
        boolean result = false;
        try {
            query = queryBuilderFactory
                    .delete()
                    .from(tableName)
                    .where(columnsAsStrings.get(Columns.billId) + " = ?")
                    .build();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, bill.getId());
            result = preparedStatement.execute();
            conn.close();
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Bill createBillObject(ResultSet resultSet) {
        Bill bill = new Bill();
        try {
            bill.setId(resultSet.getInt("bill_id"));
            bill.setItems(null);
            bill.setManager(null);
            bill.setPrivate(resultSet.getInt("private") == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }
}