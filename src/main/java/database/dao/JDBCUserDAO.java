package database.dao;

import database.entity.User;
import database.interfaces.QueryBuilder;
import database.interfaces.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JDBCUserDAO extends SampleDAO<User> implements UserDAO {

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
        facebookId("fb_id"),
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
    public boolean updateUser(User user) {
        return updateEntity(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return deleteEntity(user.getId());
    }

    @Override
    protected String getIdColumnName() {
        return Columns.facebookId.getAsString();
    }

    @Override
    protected User createEntityFromResultSet(ResultSet rs) {
        User newUser = new User();
        try {
            newUser.setId(rs.getInt(Columns.facebookId.getAsString()));
            newUser.setFirstName(rs.getString(Columns.firstName.getAsString()));
            newUser.setLastName(rs.getString(Columns.lastName.getAsString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

	@Override protected Collection<String> getColumnsForUpdate() {
		ArrayList<String> cols = new ArrayList<>();
        cols.add(Columns.firstName.getAsString());
        cols.add(Columns.lastName.getAsString());
        return cols;
	}

	@Override
    protected String buildInsertQuery(User entity) {
        try {
            return queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .column(Columns.facebookId.getAsString())
                    .column(Columns.firstName.getAsString())
                    .column(Columns.lastName.getAsString())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
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
            ps.setInt(1, Integer.parseInt(entity.getFacebookId()));
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}