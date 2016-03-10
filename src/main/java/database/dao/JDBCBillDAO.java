package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.entity.Item;
import database.entity.User;
import database.interfaces.BillDAO;
import database.interfaces.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCBillDAO extends EntityDAO<Bill> implements BillDAO {

	private static final JDBCBillDAO instance = new JDBCBillDAO();
	protected static String TABLE_NAME;

	public static JDBCBillDAO getInstance() {
		return instance;
	}

	private JDBCBillDAO() {
		TABLE_NAME = "bills";
		init();
	}

	@Override protected String getTableName() {
		return TABLE_NAME;
	}

	@Override protected String generateSqlCreateTableQuery() {
		return 	"CREATE TABLE IF NOT EXISTS `bills` (\n" +
				"  `bill_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
				"  `user_id` int(11) NOT NULL,\n" +
				"  `private` tinyint(1) NOT NULL,\n" +
				"  `open` tinyint(1) NOT NULL,\n" +
				"  PRIMARY KEY (`bill_id`),\n" +
				"  KEY `fk_users_fb_id_idx` (`user_id`),\n" +
				"  CONSTRAINT `fk_bills_users_bill_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
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

	@Override public List<Bill> getAllBills() {
		List<Bill> bills = getAllEntities();
		for(Bill bill : bills) {
			bill.populateItems();
		}
		return bills;
	}

	@Override public Bill getBill(int id) {
		Bill bill = getEntity(id);
		bill.populateItems();
		return bill;
	}

	@Override public Bill updateBill(Bill bill) {
		return updateEntity(bill);
	}

	@Override public boolean deleteBill(Bill bill) {
		return deleteEntity(bill.getID());
	}

	@Override public Bill insertBill(Bill bill) {
		insertEntity(bill);
		return bill;
	}

	@Override public Bill insertEntity(Bill bill) {
		Bill insertedBill = null;
		if (bill.getManager() != null) {
			Connection conn = DBConn.getConnection();
			try {
				String query = buildInsertQuery();
				PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				setInsertPreparedStatementParameters(ps, bill);
				int affectedRows = ps.executeUpdate();
				if (affectedRows > 0) {
					ResultSet generatedKeys = ps.getGeneratedKeys();
					if (generatedKeys.next()) {
						bill.setID(generatedKeys.getInt(1));
						insertedBill = bill;
					}
				}
				if (bill.getItems() != null) {
					insertItemsOfBill(bill);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return insertedBill;
	}

	private void insertItemsOfBill(Bill bill) {
		for (Item item : bill.getItems()) {
			item.setBillId(bill.getID());
			JDBCItemDAO.getInstance().insertItem(item);
		}
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

	@Override protected String getIdColumnName() {
		return Columns.billId.getAsString();
	}

	@Override protected Bill createEntityFromResultSet(ResultSet rs) {
		Bill newBill = new Bill();
		try {
			newBill.setID(rs.getInt(Columns.billId.getAsString()));
			User user = JDBCUserDAO.getInstance().getUser(rs.getInt(Columns.userId.getAsString()));
			newBill.setManager(user);
			newBill.setIsPrivate(rs.getBoolean(Columns.isPrivate.getAsString()));
			newBill.setIsOpen(rs.getBoolean(Columns.isOpen.getAsString()));
			newBill.addItems(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newBill;
	}

	@Override protected Collection<String> getColumnsForUpdate() {
		ArrayList<String> colsForUpdate = new ArrayList<>();
		colsForUpdate.add(Columns.isPrivate.getAsString());
		colsForUpdate.add(Columns.isOpen.getAsString());
		return colsForUpdate;
	}

	@Override protected Collection<String> getColumnsForInsert() {
		ArrayList<String> colsForInsert = new ArrayList<>();
		colsForInsert.add(Columns.userId.getAsString());
		colsForInsert.add(Columns.isPrivate.getAsString());
		colsForInsert.add(Columns.isOpen.getAsString());
		return colsForInsert;
	}

	@Override protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Bill entity) {
		try {
			ps.setBoolean(1, entity.getIsPrivate());
			ps.setBoolean(2, entity.getIsOpen());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * @param entity the new bill, should contain user with id.
	 */ protected void setInsertPreparedStatementParameters(PreparedStatement ps, Bill entity) {
		try {
			ps.setInt(1, entity.getManager().getID());
			ps.setBoolean(2, entity.getIsPrivate());
			ps.setBoolean(3, true); // Set the new bill getIsOpen value to True by default
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(new JDBCBillDAO().getColumnsForUpdate());
		System.out.println();
		System.out.println(JDBCBillDAO.getInstance().generateSqlCreateTableQuery());
	}
}
