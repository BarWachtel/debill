package database.dao;

import database.DBConn;
import database.entity.User;
import database.interfaces.UserDAO;

import java.sql.Connection;
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

    @Override
    protected String generateSqlCreateTableQuery() {
		return 	"DROP TABLE IF EXISTS 'users';\n" +
				"CREATE TABLE `users` (\n" +
                "  `user_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `username` varchar(45) DEFAULT NULL,\n" +
				"  `password` varchar(45) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`user_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
    }



	private enum Columns {
        userId("user_id"),
        username("username"),
        password("password");

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
	public User getUser(String username) {
		Connection conn = DBConn.getConnection();
		User user = null;
		try {
			String query = buildGetEntityByColumnName(Columns.username.getAsString());
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = createEntityFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.out.println("EntityDAO -> getEntity -> Exception: " + e.getMessage());
		}

		return user;
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
		return insertEntity(user);
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
            newUser.setUsername(rs.getString(Columns.username.getAsString()));
            newUser.setPassword(rs.getString(Columns.password.getAsString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

	@Override protected Collection<String> getColumnsForUpdate() {
		ArrayList<String> colsForUpdate = new ArrayList<>();
        colsForUpdate.add(Columns.username.getAsString());
        colsForUpdate.add(Columns.password.getAsString());
        return colsForUpdate;
	}

    @Override
    protected Collection<String> getColumnsForInsert() {
        List<String> colsForInsert = new ArrayList<>();
        colsForInsert.add(Columns.username.getAsString());
        colsForInsert.add(Columns.password.getAsString());
        return colsForInsert;
    }

    @Override
    protected void setUpdatePreparedStatementParameters(PreparedStatement ps, User entity) {
        try {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setInsertPreparedStatementParameters(PreparedStatement ps, User entity) {
        try{
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}