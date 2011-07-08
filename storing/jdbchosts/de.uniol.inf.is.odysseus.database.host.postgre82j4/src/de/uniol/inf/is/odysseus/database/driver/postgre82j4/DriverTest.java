package de.uniol.inf.is.odysseus.database.driver.postgre82j4;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverTest {

	public boolean test(){
		
		String url 	= "jdbc:postgresql://localhost:5432/dbit_db_0";
		String user = "dbit_admin";
		String pass = "dbit12ok";
		
		try {
			DriverManager.getConnection(url, user, pass);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
			return false;
		}
	
	}
	
	
	
}
