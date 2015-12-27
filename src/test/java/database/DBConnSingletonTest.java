package database;

import junit.framework.TestCase;
import org.testng.annotations.Test;

import java.sql.Connection;

public class DBConnSingletonTest extends TestCase {
	@Test
	public void getConnTest() {
		Connection conn = DBConn.getInstance().getConnection();
		assertNotNull(conn);
	}

}