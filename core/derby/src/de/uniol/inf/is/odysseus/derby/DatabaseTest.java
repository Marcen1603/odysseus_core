/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
