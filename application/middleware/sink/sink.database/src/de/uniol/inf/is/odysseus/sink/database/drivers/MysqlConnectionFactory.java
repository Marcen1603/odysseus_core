/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.sink.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import de.uniol.inf.is.odysseus.sink.database.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public class MysqlConnectionFactory extends AbstractDatabaseConnectionFactory {

	private HashMap<SDFDatatype, String> datatypeMappings = new HashMap<SDFDatatype, String>();

	public MysqlConnectionFactory() {
		initMappings();
	}

	@Override
	public Connection createConnection(String server, int port, String database, String user, String password) throws SQLException {
		Properties connectionProps = getCredentials(user, password);
		String connString = "jdbc:mysql://" + server + ":" + port + "/" + database;
		return DriverManager.getConnection(connString, connectionProps);
	}

	@Override
	public void createTable(Connection con, String tablename, SDFAttributeList schema) {
		try {
			Statement st = con.createStatement();
			String table = "CREATE TABLE " + tablename + "(";
			String sep = "";
			for (SDFAttribute attribute : schema) {
				table = table + sep + attribute.getAttributeName() + " ";
				table = table + this.datatypeMappings.get(attribute.getDatatype());
				sep = ", ";
			}
			table = table + ")";
			System.out.println("new TABLE: " + table);
			st.executeUpdate(table);
			System.out.println("Table creation process successfully!");
		} catch (SQLException s) {
			System.out.println("Table all ready exists!");
		}
	}

	

	private void initMappings() {
		this.datatypeMappings.put(SDFDatatype.INTEGER, "INTEGER");
		this.datatypeMappings.put(SDFDatatype.BOOLEAN, "BOOL");
		this.datatypeMappings.put(SDFDatatype.END_TIMESTAMP, "BIGINT");
		this.datatypeMappings.put(SDFDatatype.FLOAT, "FLOAT");
		this.datatypeMappings.put(SDFDatatype.LONG, "BIGINT");
		this.datatypeMappings.put(SDFDatatype.POINT_IN_TIME, "BIGINT");
		this.datatypeMappings.put(SDFDatatype.START_TIMESTAMP, "BIGINT");
		this.datatypeMappings.put(SDFDatatype.STRING, "VARCHAR(254)");
		this.datatypeMappings.put(SDFDatatype.TIMESTAMP, "BIGINT");
		this.datatypeMappings.put(SDFDatatype.DOUBLE, "DOUBLE PRECISION");
	}

}
