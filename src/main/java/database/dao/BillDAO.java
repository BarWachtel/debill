package database.dao;

import java.sql.Connection;
import database.DBConnSingleton;
import database.entity.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by user on 01/12/2015.
 */
public class BillDAO {
	private static String tableName = "bill";

	public static Collection<Bill> getAll() {
		Collection<Bill> bills = null;
		Connection conn = DBConnSingleton.getConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
			bills = new ArrayList<Bill>();
			while (resultSet.next()) {
				bills.add(createBillObject(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bills;
	}

	private static Bill createBillObject(ResultSet resultSet) {
		Bill bill = new Bill();
		try {
			bill.setId(resultSet.getInt("id"));
			bill.setItems(null);
			bill.setManager(null);
			bill.setPrivate(resultSet.getInt("private") == 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bill;
	}
}
