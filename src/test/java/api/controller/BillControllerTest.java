package api.controller;

import database.entity.Bill;
import junit.framework.TestCase;

import java.util.List;

public class BillControllerTest extends TestCase {

	public void testGetAll() throws Exception {
		List<Bill> bills = BillController.getAll();
		assertNotNull(bills);
		assertTrue(bills.size() > 0);
	}
}