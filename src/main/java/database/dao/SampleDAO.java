package database.dao;

import database.DBConn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SampleDAO {

    protected static String TABLE_NAME = null;

    public int createTable() {
        Connection connection = DBConn.getConnection();
        int successs = 0;
        try {
            Statement st = connection.createStatement();
            String sql = generateSqlCreateTableQuery();
            successs = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return successs;
    }

    protected abstract String generateSqlCreateTableQuery();
}
