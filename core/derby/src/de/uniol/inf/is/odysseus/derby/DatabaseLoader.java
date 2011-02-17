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

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseLoader {
	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

	public static void loadDatabase() {
		System.out.println("Starting up Derby Database...");
		try {
			Class.forName(DRIVER).newInstance();
			System.out.println("Derby Database started.");
		} catch (ClassNotFoundException cnfe) {
			System.err.println("\nUnable to load the JDBC driver " + DRIVER);
			System.err.println("Please check your CLASSPATH.");
			cnfe.printStackTrace(System.err);
		} catch (InstantiationException ie) {
			System.err.println("\nUnable to instantiate the JDBC driver " + DRIVER);
			ie.printStackTrace(System.err);
		} catch (IllegalAccessException iae) {
			System.err.println("\nNot allowed to access the JDBC driver " + DRIVER);
			iae.printStackTrace(System.err);
		}
	}

	public static void stopDatabase() {
		System.out.println("Shutting down Derby Database...");
		try {
			DriverManager.getConnection(Activator.PROTOCOL+":;shutdown=true");
		} catch (SQLException e) {
			if (((e.getErrorCode() == 50000) && ("XJ015".equals(e.getSQLState())))) {
				// we got the expected exception
				System.out.println("Derby shut down normally");
				// Note that for single database shutdown, the expected
				// SQL state is "08006", and the error code is 45000.
			} else {
				// if the error code or SQLState is different, we have
				// an unexpected exception (shutdown failed)
				System.err.println("Derby did not shut down normally");
				e.printStackTrace(System.err);
			}
		}
		System.out.println("Derby Database stopped.");
	}

}
