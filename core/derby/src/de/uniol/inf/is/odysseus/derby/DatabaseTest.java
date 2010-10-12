package de.uniol.inf.is.odysseus.derby;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTest {

	private Connection connection;

	public DatabaseTest(Connection conn) {
		this.connection = conn;
	}
	
	public static void runTest(Connection c){
		DatabaseTest t = new DatabaseTest(c);
		t.createTable();
		t.addAttributes();
		t.selectAttributes();
	}

	public void createTable() {
		try {
			Statement s = this.connection.createStatement();
			s.executeUpdate("CREATE TABLE Address (ID INT, StreetName VARCHAR(20), City VARCHAR(20))");
			System.out.println("Table created.");
			s.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addAttributes() {
		try {
			Statement s = this.connection.createStatement();
			int c = s.executeUpdate("INSERT INTO Address (ID, StreetName, City)VALUES (2, '25 Bay St.', 'Hull')");
			System.out.println("Number of rows inserted: " + c);
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void selectAttributes() {
		try {
			Statement s = this.connection.createStatement();
			ResultSet res = s.executeQuery("SELECT * FROM Address");
			System.out.println("List of Addresses: ");
			while (res.next()) {
				System.out.println("  " + res.getInt("ID") + ", " + res.getString("StreetName") + ", " + res.getString("City"));
			}
			res.close();
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
