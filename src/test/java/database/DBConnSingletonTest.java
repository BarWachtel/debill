package database;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.Connection;

public class DBConnSingletonTest extends TestCase {
	@Test
	public void getConnTest() {
		Connection conn = DBConnSingleton.getInstance().getConnection();
		assertNotNull(conn);
	}

}