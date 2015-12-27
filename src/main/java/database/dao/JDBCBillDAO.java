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

	static {
		TABLE_NAME = "bills";
	}

    public JDBCBillDAO(){
    }

	private enum Columns {
		billId ("bill_id"),
		fbId ("fb_id"),
		isPrivate ("private");

		private String asString;

		Columns(String asString) {
			this.asString = asString;
		}

		public String getAsString() {
			return asString;
		}
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

    public static void main(String[] args) {
        System.out.println(new JDBCBillDAO().getColumnsForUpdate());
    }

    @Override
    protected String buildInsertQuery(Bill entity) {
		String query = null;
        try {
            query = queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .value(Columns.billId.getAsString() + "=" + entity.getId())
                    .value(Columns.isPrivate.getAsString() +"="+entity.isPrivate())
                    .build();
        } catch (QueryBuilderException e) {
            e.printStackTrace();
        }
		return query;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Bill entity) {

    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, Bill entity) {

    }

    public static List<Bill> getAllBills() {
        Connection conn = DBConn.getConnection();
		List<Bill> bills = null;
		try {
            String query = queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .build();
            ResultSet resultSet = conn.createStatement().executeQuery(query);
            bills = new ArrayList<>();
            while (resultSet.next()) {
                bills.add(createBillObject(resultSet));
            }
            conn.close();
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public static Bill getBill(int id) {
        Connection conn = DBConn.getConnection();
        Bill bill = null;
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(TABLE_NAME)
                    .where(Columns.billId.getAsString() + "=" + id)
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
                    .from(TABLE_NAME)
                    .set(Columns.isPrivate.getAsString() + " = ?")
                    .where(Columns.billId.getAsString() + " = ?")
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
                    .from(TABLE_NAME)
                    .where(Columns.billId.getAsString() + " = ?")
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

	@Override
	public int insertBill(Bill bill) {
		// Should return new entity ID
		return -1;
	}

	private static Bill createBillObject(ResultSet resultSet) {
        Bill bill = new Bill();
        try {
            bill.setId(resultSet.getInt(Columns.billId.getAsString()));
            bill.setItems(null);
            bill.setManager(null);
            bill.setPrivate(resultSet.getInt(Columns.isPrivate.getAsString()) == 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }
}
