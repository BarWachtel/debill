import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnSingleton {

    public static void main(String[] args) {
        Connection conn = DBConnSingleton.getInstance().getConnection();
        System.out.println("got connection. " + conn.toString());
    }

    private static final String URL = "jdbc:mysql://localhost/";
    private static final String FILE_NAME = "db_credentials.txt";

    private static Connection conn;

    private static String DB_NAME;
    private static String USER_NAME;
    private static String PASSWORD;

    private static DBConnSingleton instance = null;

    public static synchronized DBConnSingleton getInstance() {
        if(instance==null){
            instance = new DBConnSingleton();
        }
        return instance;
    }

    public static synchronized  Connection getConnection() {
        return conn;
    }

    static {
        try {
            System.out.println("Loading the driver...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully");
            readCredentialsFromFile();
            System.out.println("Credentials read successfully - " +
                    "db name = " + DB_NAME +
                    " , user = " + USER_NAME +
                    " , password = " + PASSWORD);
            conn = DriverManager.getConnection(URL+DB_NAME, USER_NAME, PASSWORD);
            System.out.println((new StringBuilder("conn success. conn=")).append(conn).toString());
        } catch (Exception ex) {
            System.err.println((new StringBuilder("error loading:")).append(ex.getMessage()).toString());
        }
    }

    private static void readCredentialsFromFile() throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IOException("No db name in db_credentials file");
            }
            DB_NAME = line;

            line = bufferedReader.readLine();
            if (line == null) {
                throw new IOException("No user name in db_credentials file");
            }
            USER_NAME = line;
            if (line == null) {
                System.out.println("No password in db_credentials file");
            }

            line = bufferedReader.readLine();
            PASSWORD = line;
        } finally {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }
}
