package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.entity.Item;
import database.interfaces.ItemDAO;
import database.interfaces.QueryBuilder.QueryBuilderException;
import database.querybuilder.QueryBuilderFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCItemDAO implements ItemDAO {

    private enum Columns {
        itemId ("item_id"),
        billId ("bill_id"),
        name ("name"),
        price ("price"),
        amount ("amount");

		String asString;

		Columns(String asString) {
			this.asString = asString;
		}

		public String getAsString() {
			return asString;
		}
	}

    private static final String tableName;
    private static final QueryBuilderFactory queryBuilderFactory;
//    private static final Map<Columns, String> columnsToStrings;

    static {
        tableName = "items";
        queryBuilderFactory = new QueryBuilderFactory();
//        columnsToStrings = new HashMap<>();
//        columnsToStrings.put(Columns.itemId, "item_id");
//        columnsToStrings.put(Columns.billId, "bill_id");
//        columnsToStrings.put(Columns.name, "name");
//        columnsToStrings.put(Columns.price, "price");
//        columnsToStrings.put(Columns.amount, "amount");
    }

   // @Override
    public static List<Item> getAllItems(Bill bill) {
        Connection conn = DBConn.getConnection();
        List<Item> items = new ArrayList<>();
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(tableName)
                    .build();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                items.add(createItemFromResultSet(resultSet));
            }
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }

        return items;
    }

   // @Override
    public static Item getItem(Bill bill, int itemId) {
        Connection conn = DBConn.getConnection();
        Item item = null;
        try {
            String query = queryBuilderFactory
                    .select()
                    .from(tableName)
                    .where(Columns.billId.getAsString() + " = " + bill.getId())
                    .where(Columns.itemId.getAsString() + " = " + itemId)
                    .build();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                item = createItemFromResultSet(resultSet);
            }
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }

        return item;
    }

    @Override
    public boolean updateItem(Item item) {
        Connection conn = DBConn.getConnection();
        String query;
        int resultSet = 0;
        try {
            query = queryBuilderFactory
                    .update()
                    .from(tableName)
                    .set(Columns.price.getAsString() + " = ?")
                    .set(Columns.name.getAsString() + " = ?")
                    .set(Columns.amount.getAsString() + " = ?")
                    .build();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setFloat(1, item.getPrice());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setInt(3, item.getQuantity());
            resultSet = preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return resultSet == 1;
    }

    @Override
    public boolean deleteItem(Item item) {
        Connection conn = DBConn.getConnection();
        String query;
        boolean result = false;
        try {
            query = queryBuilderFactory
                    .delete()
                    .from(tableName)
                    .where(Columns.billId.getAsString() + " = ?")
                    .where(Columns.itemId.getAsString() + " = ?")
                    .build();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,item.getBillId());
            preparedStatement.setInt(2,item.getId());
            result = preparedStatement.execute();
            conn.close();
        } catch(SQLException | QueryBuilderException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Item createItemFromResultSet(ResultSet resultSet) {
        Item item = new Item();
        try {
            item.setId(resultSet.getInt(Columns.itemId.getAsString()));
            item.setBillId(resultSet.getInt(Columns.billId.getAsString()));
            item.setName(resultSet.getString(Columns.name.getAsString()));
            item.setPrice(resultSet.getFloat(Columns.price.getAsString()));
            item.setQuantity(resultSet.getInt(Columns.amount.getAsString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
}