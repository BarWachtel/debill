package database.dao;

import database.DBConn;
import database.entity.Item;
import database.interfaces.ItemDAO;
import database.interfaces.QueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCItemDAO extends EntityDAO<Item> implements ItemDAO {

    private static final JDBCItemDAO instance = new JDBCItemDAO();

    public static JDBCItemDAO getInstance() {
        return instance;
    }
	protected static String TABLE_NAME;

	@Override protected String getTableName() {
		return TABLE_NAME;
	}
    private JDBCItemDAO() {
		TABLE_NAME = "items";
		init();
	}

	@Override
    protected String generateSqlCreateTableQuery() {
		return 	"CREATE TABLE `items` (\n" +
                "  `item_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `bill_id` int(11) NOT NULL,\n" +
                "  `name` varchar(45) NOT NULL,\n" +
                "  `price` float NOT NULL,\n" +
                "  `quantity` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`item_id`),\n" +
                "  KEY `fk_items_bills_bill_id_idx` (`bill_id`),\n" +
                "  CONSTRAINT `fk_items_bills_bill_id` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`bill_id`) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
    }

    private enum Columns {
        itemId("item_id"),
        billId("bill_id"),
        name("name"),
        price("price"),
        quantity("quantity");

        String asString;

        Columns(String asString) {
            this.asString = asString;
        }

        public String getAsString() {
            return asString;
        }
    }

    @Override
    public List<Item> getAllItems(int billId) {
        Connection conn = DBConn.getConnection();
        List<Item> items = new ArrayList<>();
        try {
            String query = queryBuilderFactory.select().from(TABLE_NAME).where(Columns.billId.getAsString()+"=?;").build();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(createEntityFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("EntityDAO -> getAllEntities -> Exception: " + e.getMessage());
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public Item getItem(int itemId) {
        return getEntity(itemId);
    }

    @Override
    public Item updateItem(Item item) {
        return updateEntity(item);
    }

    @Override
    public boolean deleteItem(Item item) {
        return deleteEntity(item.getID());
    }

    @Override
    public Item insertItem(Item item) {
        insertEntity(item);
        return item;
    }

    @Override
    protected String getIdColumnName() {
        return Columns.itemId.getAsString();
    }

    @Override
    protected Item createEntityFromResultSet(ResultSet rs) {
        Item newItem = new Item();
        try {
            newItem.setID(rs.getInt(Columns.itemId.getAsString()));
            newItem.setBillId(rs.getInt(Columns.billId.getAsString()));
            newItem.setName(rs.getString(Columns.name.getAsString()));
            newItem.setPrice(rs.getFloat(Columns.price.getAsString()));
            newItem.setQuantity(rs.getInt(Columns.quantity.getAsString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newItem;
    }

    @Override
    protected Collection<String> getColumnsForUpdate() {
        ArrayList<String> cols = new ArrayList<>();
        cols.add(Columns.name.getAsString());
        cols.add(Columns.price.getAsString());
        cols.add(Columns.quantity.getAsString());
        return cols;
    }

    @Override
    protected Collection<String> getColumnsForInsert() {
        List<String> colsForInsert = new ArrayList<>();
        colsForInsert.add(Columns.billId.getAsString());
        colsForInsert.add(Columns.name.getAsString());
        colsForInsert.add(Columns.price.getAsString());
        colsForInsert.add(Columns.quantity.getAsString());
        return colsForInsert;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Item entity) {
        try {
            ps.setString(1, entity.getName());
            ps.setFloat(2, entity.getPrice());
            ps.setInt(3, entity.getQuantity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, Item entity) {
        try {
            ps.setInt(1, entity.getBillId());
            ps.setString(2, entity.getName());
            ps.setFloat(3, entity.getPrice());
            ps.setInt(4, entity.getQuantity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}