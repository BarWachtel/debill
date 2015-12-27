package database.dao;

import database.entity.Bill;
import database.entity.Item;
import database.interfaces.ItemDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCItemDAO extends SampleDAO<Item> implements ItemDAO {

    private static final JDBCItemDAO instance = new JDBCItemDAO();

    public static JDBCItemDAO getInstance() {
        return instance;
    }

    private JDBCItemDAO() {}

    static {
        TABLE_NAME = "items";
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
    public List<Item> getAllItems(Bill bill) {
        return getAllEntities();
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
        return deleteEntity(item.getId());
    }

    @Override
    public Item insertItem(Item item) {
        return insertEntity(item);
    }

    @Override
    protected String getIdColumnName() {
        return Columns.itemId.getAsString();
    }

    @Override
    protected Item createEntityFromResultSet(ResultSet rs) {
        Item newItem = new Item();
        try {
            newItem.setId(rs.getInt(Columns.itemId.getAsString()));
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
            ps.setInt(1, entity.getId());
            ps.setInt(2, entity.getBillId());
            ps.setString(3, entity.getName());
            ps.setFloat(4, entity.getPrice());
            ps.setInt(5, entity.getQuantity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}