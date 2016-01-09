package database.dao;

import database.entity.User;
import database.interfaces.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCUserDAO extends EntityDAO<User> implements UserDAO {

    private static final JDBCUserDAO instance = new JDBCUserDAO();

    public static JDBCUserDAO getInstance() {
        return instance;
    }

    private JDBCUserDAO() {
    }

	static {
		TABLE_NAME = "users";
	}

    private enum Columns {
        userId("user_id"),
        firstName("first_name"),
        lastName("last_name");

        private String asString;

        Columns(String asString) {
            this.asString = asString;
        }

        public String getAsString() {
            return asString;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return getAllEntities();
    }

    @Override
    public User getUser(int id) {
        return getEntity(id);
    }

    @Override
    public User updateUser(User user) {
        return updateEntity(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return deleteEntity(user.getID());
    }

    @Override
    public User insertUser(User user) {
        insertEntity(user);
        return user;
    }

    @Override
    protected String getIdColumnName() {
        return Columns.userId.getAsString();
    }

    @Override
    protected User createEntityFromResultSet(ResultSet rs) {
        User newUser = new User();
        try {
            newUser.setID(rs.getInt(Columns.userId.getAsString()));
            newUser.setFirstName(rs.getString(Columns.firstName.getAsString()));
            newUser.setLastName(rs.getString(Columns.lastName.getAsString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

	@Override protected Collection<String> getColumnsForUpdate() {
		ArrayList<String> colsForUpdate = new ArrayList<>();
        colsForUpdate.add(Columns.firstName.getAsString());
        colsForUpdate.add(Columns.lastName.getAsString());
        return colsForUpdate;
	}

    @Override
    protected Collection<String> getColumnsForInsert() {
        List<String> colsForInsert = new ArrayList<>();
        colsForInsert.add(Columns.firstName.getAsString());
        colsForInsert.add(Columns.lastName.getAsString());
        return colsForInsert;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, User entity) {
        try {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, User entity) {
        try{
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}