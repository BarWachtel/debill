package database.dao;

import database.DBConn;
import database.entity.Item;
import database.entity.ItemSummary;
import database.entity.BillSummary;
import database.entity.User;
import database.interfaces.QueryBuilder;
import database.interfaces.SummaryDAO;
import database.querybuilder.QueryBuilderFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCSummaryDAO extends EntityDAO<ItemSummary> implements SummaryDAO {

	private static final JDBCSummaryDAO instance = new JDBCSummaryDAO();
	private static final QueryBuilderFactory queryBuilderFactory = new QueryBuilderFactory();

	public static JDBCSummaryDAO getInstance() {
		return instance;
	}
	protected static String TABLE_NAME;

	@Override protected String getTableName() {
		return TABLE_NAME;
	}

	private JDBCSummaryDAO() {
		TABLE_NAME = "bills_summary";
		init();
	}

	private enum Columns {
		summaryId("bills_summary_id"),
		billId("bill_id"),
		itemId("item_id"),
		userId("user_id"),
		quantity("quantity");

		private String asString;

		Columns(String asString) {
			this.asString = asString;
		}

		public String getAsString() {
			return asString;
		}
	}

	@Override public BillSummary getSummaryByBillId(int billId) {
		Connection connection = DBConn.getConnection();
		BillSummary summary = new BillSummary();
		try {
			String query = queryBuilderFactory.select().from(TABLE_NAME).where(Columns.billId.getAsString() + "=?").build();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, billId);
			ResultSet rs = ps.executeQuery();
			updateSummaryWithResultSet(summary, rs);
		} catch (QueryBuilder.QueryBuilderException | SQLException e) {
			e.printStackTrace();
		}
		return summary;
	}

	@Override public BillSummary getSummaryByBillIdAndUserId(int billId, int userId) {
		Connection connection = DBConn.getConnection();
		BillSummary summary = new BillSummary();
		try {
			String query = queryBuilderFactory.select().from(TABLE_NAME).where(Columns.billId.getAsString() + "=?").where(Columns.userId.getAsString() + "=?")
					.build();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, billId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			updateSummaryWithResultSet(summary, rs);
		} catch (QueryBuilder.QueryBuilderException | SQLException e) {
			e.printStackTrace();
		}
		return summary;
	}

    @Override
    public ItemSummary addItemToSummary(ItemSummary itemSummary) {
        insertEntity(itemSummary);
        return itemSummary;
    }

    @Override
    public boolean deleteItemFromSummary(ItemSummary itemSummary) {
        return deleteEntity(itemSummary.getID());
    }

    @Override
    protected String getIdColumnName() {
        return Columns.summaryId.getAsString();
    }

    @Override
    protected ItemSummary createEntityFromResultSet(ResultSet rs) {
        return null;
    }

    @Override
    protected Collection<String> getColumnsForUpdate() {
        return null;
    }

    @Override
    protected Collection<String> getColumnsForInsert() {
        List<String> cols = new ArrayList<>();
        cols.add(Columns.billId.getAsString());
        cols.add(Columns.itemId.getAsString());
        cols.add(Columns.userId.getAsString());
        cols.add(Columns.quantity.getAsString());
        return null;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, ItemSummary entity) {

    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, ItemSummary entity) {
        try {
            ps.setInt(1, entity.getItem().getBillId());
            ps.setInt(2, entity.getItem().getID());
            ps.setInt(3, entity.getUser().getID());
            ps.setInt(4, entity.getPaidForQuantityByUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String generateSqlCreateTableQuery() {
        return "CREATE TABLE `bills_summary` (\n" +
                "  `bill_summary_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `bill_id` int(11) NOT NULL,\n" +
                "  `item_id` int(11) NOT NULL,\n" +
                "  `user_id` int(11) NOT NULL,\n" +
                "  `quantity` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`bill_summary_id`),\n" +
                "  KEY `fk_bills_summary_bills_bill_id_idx` (`bill_id`),\n" +
                "  KEY `fk_bills_summary_items_item_id_idx` (`item_id`),\n" +
                "  KEY `fk_bills_summary_users_user_id_idx` (`user_id`),\n" +
                "  CONSTRAINT `fk_bills_summary_bills_bill_id` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`bill_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_bills_summary_items_item_id` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT `fk_bills_summary_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
    }

    private void updateSummaryWithResultSet(BillSummary summary, ResultSet rs) {
        try {
            while (rs.next()) {
                User user = JDBCUserDAO.getInstance().getUser(rs.getInt(Columns.userId.getAsString()));
                Item item = JDBCItemDAO.getInstance().getItem(rs.getInt(Columns.itemId.getAsString()));
                int quantity = rs.getInt(Columns.quantity.getAsString());
                summary.addItemSummary(new ItemSummary(user, item, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
