package database.dao;

import database.DBConn;
import database.entity.Bill;
import database.entity.Group;
import database.entity.User;
import database.interfaces.GroupDAO;
import database.interfaces.QueryBuilder;
import database.querybuilder.QueryBuilderFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCGroupDAO extends SampleDAO<Group> implements GroupDAO {

    private static final JDBCGroupDAO instance = new JDBCGroupDAO();
    private static final QueryBuilderFactory queryBuilderFactory = new QueryBuilderFactory();

    public static JDBCGroupDAO getInstance() {
        return instance;
    }

    static {
        TABLE_NAME = "bills_groups";
    }

    private enum Columns {
        groupId("group_id"),
        billId("bill_id"),
        userId("user_id");

        private String asString;

        Columns(String asString) {
            this.asString = asString;
        }

        public String getAsString() {
            return asString;
        }
    }

    private JDBCGroupDAO() {
    }

    @Override
    public Group getGroupUsersByBillId(int billId) {
        Connection conn = DBConn.getConnection();
        Group selectedGroup = new Group();
        try {
            String query = queryBuilderFactory.select().from(TABLE_NAME).where(Columns.billId.getAsString() + "=?").build();
            PreparedStatement ps = conn.prepareStatement(query, Statement.NO_GENERATED_KEYS);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                Bill bill = JDBCBillDAO.getInstance().getBill(rs.getInt(Columns.billId.getAsString()));
                selectedGroup.setBill(bill);
            }
            do {
                List<User> users = new ArrayList<>();
                User user = JDBCUserDAO.getInstance().getUser(rs.getInt(Columns.userId.getAsString()));
                users.add(user);
            } while (rs.next());
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return selectedGroup;
    }

    @Override
    public Group addUserToGroup(int billId, int userId) {
        Connection conn = DBConn.getConnection();
        Group updatedGroup = null;
        try {
            List<String> cols = new ArrayList<>();
            cols.add(Columns.billId.getAsString());
            cols.add(Columns.userId.getAsString());
            String query = queryBuilderFactory.insert().into(TABLE_NAME).column(cols).build();
            PreparedStatement ps = conn.prepareStatement(query, Statement.NO_GENERATED_KEYS);
            ps.setInt(1, billId);
            ps.setInt(2, userId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                updatedGroup = getGroupUsersByBillId(billId);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("SampleEntityDAO -> insertEntity -> Exception: " + e.getMessage());
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return updatedGroup;
    }

    @Override
    public Group deleteUserFromGroup(int billId, int userId) {
        Connection connection = DBConn.getConnection();
        Group updatedGroup = null;
        try {
            String query = queryBuilderFactory
                    .delete()
                    .from(TABLE_NAME)
                    .where(Columns.billId.getAsString() + "=?")
                    .where(Columns.userId.getAsString() + "=?")
                    .build();
            PreparedStatement ps = connection.prepareStatement(query, Statement.NO_GENERATED_KEYS);
            ps.setInt(1, billId);
            ps.setInt(2, userId);
            boolean success = ps.execute();
            if (!success) {
                System.out.println("deleteUserFromGroup -> billId: " + billId + " userId: " + userId + " - > Something want wrong!");
            }
            updatedGroup = getGroupUsersByBillId(billId);
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedGroup;
    }

    @Override
    public boolean deleteGroupByBillId(int billId) {
        Connection connection = DBConn.getConnection();
        boolean success = false;
        try {
            String query = queryBuilderFactory
                    .delete()
                    .from(TABLE_NAME)
                    .where(Columns.billId.getAsString() + "=?")
                    .build();
            PreparedStatement ps = connection.prepareStatement(query, Statement.NO_GENERATED_KEYS);
            ps.setInt(1, billId);
            success = ps.execute();
            if (!success) {
                System.out.println("deleteGroupByBillId -> billId: " + billId + " - > Something want wrong!");
            }
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }
}
