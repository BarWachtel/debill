package database.dao;

import database.entity.User;
import database.interfaces.QueryBuilder;
import database.interfaces.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCUserDAO extends SampleDAO<User> implements UserDAO {

    private static final String COL_FB_UID = "fb_id";
    private static final String COL_FIRST_NAME = "first_name";
    private static final String COL_LAST_NAME = "last_name";

    public JDBCUserDAO() {
        super("users");
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
        return deleteUser(user);
    }

    @Override
    protected String getIdColumnName() {
        return COL_FB_UID;
    }

    @Override
    protected User createEntityFromResultSet(ResultSet rs) {
        User newUser = new User();
        try {
            newUser.setId(rs.getInt(COL_FB_UID));
            newUser.setFirstName(rs.getString(COL_FIRST_NAME));
            newUser.setLastName(rs.getString(COL_LAST_NAME));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    protected String buildUpdateQuery(User entity) {
        try {
            return queryBuilderFactory
                    .update()
                    .from(TABLE_NAME)
                    .where(COL_FB_UID + "=" + entity.getId())
                    .set(COL_FIRST_NAME + "=" + entity.getFirstName())
                    .set(COL_LAST_NAME + "=" + entity.getLastName())
                    .build();
        } catch (QueryBuilder.QueryBuilderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String buildInsertQuery(User entity) {
        try {
            return queryBuilderFactory
                    .insert()
                    .into(TABLE_NAME)
                    .column(COL_FB_UID)
                    .column(COL_FIRST_NAME)
                    .column(COL_LAST_NAME)
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