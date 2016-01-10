package database.dao;

import database.DBConn;

import java.sql.*;

public abstract class SampleDAO {

    protected String TABLE_NAME = null;

    public SampleDAO() {
        if(!isTableExists()) {
            createTable();
        }
    }

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
