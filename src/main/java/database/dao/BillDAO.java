package database.dao;

import java.sql.Connection;
import database.DBConnSingleton;
import database.entity.Bill;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 01/12/2015.
 */
public class BillDAO {
	private static String tableName = "bill";

	public void populateItems() {
		// Requires another query
	}

	public void populateManager() {
		// Requires another query
	}

	public static List<Bill> getAll() {
		List<Bill> bills = null;
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

	public static Bill get(int id) {
		Bill bill = null;
		Connection conn = DBConnSingleton.getConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet =
					statement.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + id);
			if (resultSet.next()) {
				bill = createBillObject(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bill;
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
