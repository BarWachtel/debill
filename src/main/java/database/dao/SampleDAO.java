package database.dao;

import database.DBConn;

import java.sql.*;

public abstract class SampleDAO {

    protected String TABLE_NAME = null;

    public boolean isTableExists() {
        Connection connection = DBConn.getConnection();
        boolean tableExists = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, TABLE_NAME, null);
            if(resultSet.next()) {
                tableExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableExists;
    }

    public int createTable() {
        Connection connection = DBConn.getConnection();
        int success = 0;
        try {
            Statement st = connection.createStatement();
            String sql = generateSqlCreateTableQuery();
            success = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    protected abstract String generateSqlCreateTableQuery();
}
