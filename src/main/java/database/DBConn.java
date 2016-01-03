package database;

//import database.DatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBConn {
	private static final String URL = "jdbc:mysql://localhost/";
	//	private static final String FILE_NAME = "db_credentials.txt";

	private static Connection conn;

	private static String DB_NAME;
	private static String USER_NAME;
	private static String PASSWORD;
	private static final Lock instanceLock = new ReentrantLock();
	private static DBConn instance = null;

	private static void SetDatabaseCredentials() {
		DB_NAME = DatabaseCredentials.DB_NAME;
		USER_NAME = DatabaseCredentials.USER_NAME;
		PASSWORD = DatabaseCredentials.PASSWORD;

		System.out.println("Credentials read successfully - " +
				"db name = " + DB_NAME +
				" , user = " + USER_NAME +
				" , password = " + PASSWORD);
	}

	public static synchronized Connection getConnection() {
		if (conn == null) {
			try {
				SetDatabaseCredentials();
				conn = DriverManager.getConnection(URL + DB_NAME, USER_NAME, PASSWORD);
				System.out.println((new StringBuilder("conn success. conn=")).append(conn).toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return conn;
	}

	static {
		try {
			System.out.println("Loading the driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded successfully");
		} catch (Exception ex) {
			System.err.println((new StringBuilder("error loading:")).append(ex.getMessage()).toString());
		}
	}

	//	private static void readCredentialsFromFile() throws IOException {
	//		BufferedReader bufferedReader = null;
	//		try {
	//			bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
	//			String line = bufferedReader.readLine();
	//			if (line == null) {
	//				throw new IOException("No db name in db_credentials file");
	//			}
	//			DB_NAME = line;
	//
	//			line = bufferedReader.readLine();
	//			if (line == null) {
	//				throw new IOException("No user name in db_credentials file");
	//			}
	//			USER_NAME = line;
	//			if (line == null) {
	//				System.out.println("No password in db_credentials file");
	//			}
	//
	//			line = bufferedReader.readLine();
	//			PASSWORD = line;
	//		} finally {
	//			if (bufferedReader != null) {
	//				bufferedReader.close();
	//			}
	//		}
	//	}
}
