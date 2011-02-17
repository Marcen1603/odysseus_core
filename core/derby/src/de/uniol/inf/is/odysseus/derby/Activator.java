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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final String DB_NAME = "odysseusDB";
	private static final String DB_USER = "odysseus";
	private static final String DB_PASS = "penelope";
	public static final String PROTOCOL = "jdbc:derby";
	
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	private static Connection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		try {

			String home = System.getProperty("user.home", ".");
			String system = home + File.separatorChar + ".database";
			System.setProperty("derby.system.home", system);

			DatabaseLoader.loadDatabase();

			Properties props = new Properties();
			props.put("user", DB_USER);
			props.put("password", DB_PASS);
			connection = DriverManager.getConnection(PROTOCOL + ":" + DB_NAME + ";create=true", props);

			//DatabaseTest.runTest(connection);
			
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		DatabaseLoader.stopDatabase();
		Activator.connection = null;
	}

	public static Connection getConnection() {
		return connection;
	}

}
