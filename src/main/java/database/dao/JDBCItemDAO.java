package database.dao;

import database.entity.Bill;
import database.entity.Item;
import database.interfaces.ItemDAO;
import database.interfaces.QueryBuilder.QueryBuilderException;

import java.sql.*;
import java.util.*;

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
    public boolean updateItem(Item item) {
        return updateEntity(item);
    }

    @Override
    public boolean deleteItem(Item item) {
        return deleteEntity(item.getId());
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
        ArrayList cols = new ArrayList();
        cols.add(Columns.price.getAsString());
        cols.add(Columns.quantity.getAsString());
        return cols;
    }

    @Override
    protected String buildInsertQuery(Item entity) {
        String query = null;
        try {
            query = queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .column(Columns.itemId.getAsString())
                    .column(Columns.billId.getAsString())
                    .column(Columns.name.getAsString())
                    .column(Columns.price.getAsString())
                    .column(Columns.quantity.getAsString())
                    .build();
        } catch (QueryBuilderException e) {
            e.printStackTrace();
        }
        return query;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, Item entity) {
        try {
            ps.setFloat(1, entity.getPrice());
            ps.setInt(2, entity.getQuantity());
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