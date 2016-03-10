package database.dao;

import database.DBConn;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CommonDAO {
	private static boolean IN_DEVELOPMENT = false;
	private static Logger log = Logger.getGlobal();

    public CommonDAO() {
    }

	protected void init() {
		log.info("Initializing CommonDAO - " + getTableName());
        createTable();
	}

	protected abstract String getTableName();

    public boolean isTableExists() {
        Connection connection = DBConn.getConnection();
        boolean tableExists = false;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, getTableName(), null);
            if(resultSet.next()) {
                tableExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableExists;
    }

    public int createTable() {
		log.info("Creating table " + getTableName());
        Connection connection = DBConn.getConnection();
        int success = 0;
        try {
            Statement stmt = connection.createStatement();

			if (IN_DEVELOPMENT) {
				String dropTableQuery = "DROP TABLE IF EXISTS " + getTableName() + ";";
				stmt.executeUpdate(dropTableQuery);
			}

            String createTableQuery = generateSqlCreateTableQuery();
            success = stmt.executeUpdate(createTableQuery);

			log.info("Executed statement - ");
			log.info(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    protected abstract String generateSqlCreateTableQuery();
}
