/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.database.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
public class DatabaseConnection implements IDatabaseConnection {

	private static Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

	private boolean connected = false;
	// do NOT change this to protected or public!!
	private Connection connection;
	// protected HashMap<SDFDatatype, String> datatypeMappings = new
	// HashMap<SDFDatatype, String>();

	private String connString;

	private Properties connectionProps;

	public DatabaseConnection(String jdbcString, Properties props) {
		this.connString = jdbcString;
		this.connectionProps = props;
	}

	public DatabaseConnection(String jdbcString, String username, String password) {
		this.connString = jdbcString;
		connectionProps = new Properties();
		connectionProps.put("user", username);
		connectionProps.put("password", password);
	}

	public DatabaseConnection(String jdbcString, String username) {
		this.connString = jdbcString;
		connectionProps = new Properties();
	}

	@Override
	public Connection getConnection() throws SQLException {
		assertConnection();
		return this.connection;
	}

	public boolean testConnection() {
		try {
			assertConnection();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public String getDBMSSpecificType(int jdbcType) throws SQLException {
		assertConnection();
		DatabaseMetaData dmd = this.connection.getMetaData();
		ResultSet r = dmd.getTypeInfo();
		while (r.next()) {
			int type = r.getInt("DATA_TYPE");
			String dbmsSpecificName = r.getString("TYPE_NAME");

			String params = r.getString("CREATE_PARAMS").trim();
			String fullName = dbmsSpecificName;
			if (!params.isEmpty()) {
				if (params.startsWith("(M)")) {
					fullName = fullName + "(128)";
				}
			}
			// System.out.println(r.getString("TYPE_NAME") + " | " +
			// r.getString("CREATE_PARAMS")+" --> "+fullName);
			if (type == jdbcType) {
				return fullName;
			}
		}
		return null;
	}

	@Override
	public void createTable(String tablename, SDFSchema schema) throws SQLException {
		assertConnection();
		Statement st = connection.createStatement();
		String table = "CREATE TABLE " + tablename + "(";
		String sep = "";
		for (SDFAttribute attribute : schema) {
			table = table + sep + attribute.getAttributeName() + " ";
			table = table + getDBMSSpecificType(DatatypeRegistry.getDataHandler(attribute.getDatatype()).getDefaultSQLDatatype());
			sep = ", ";
		}
		table = table + ")";
		st.executeUpdate(table);
	}

	@Override
	public boolean tableExists(String tablename) {
		try {
			assertConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet res = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (res.next()) {
				if (res.getString("TABLE_NAME").equals(tablename)) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public SDFSchema getSchema(String tablename) throws SQLException {
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		assertConnection();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = meta.getColumns(null, null, tablename, null);
		while (rs.next()) {
			String name = rs.getString("COLUMN_NAME");
			SDFDatatype dt = DatatypeRegistry.getSDFDatatype(rs.getInt("DATA_TYPE"));
			SDFAttribute a = new SDFAttribute(null, name, dt);
			attrs.add(a);
		}
		SDFSchema schema = new SDFSchema(tablename, attrs);
		return schema;
	}

	@Override
	public boolean equalSchemas(String tablename, SDFSchema schema) throws SQLException {

		assertConnection();
		DatabaseMetaData meta = connection.getMetaData();
		int i = 0;
		ResultSet rsColumns = meta.getColumns(null, null, tablename, null);
		while (rsColumns.next()) {
			if (i >= schema.size()) {
				// unterschiedliche anzahl
				return false;
			}
			int dbType = rsColumns.getInt("DATA_TYPE");
			SDFDatatype dt = schema.get(i).getDatatype();

			if (!DatatypeRegistry.mappingExists(dt, dbType)) {
				// both combinations were not found
				logger.error("Expected types for stream and database are not equal for");
				logger.error("- database: " + dbType + " <--> local: " + dt);
				logger.error("- see also in java.sql.Types!");
				logger.error("- database: " + rsColumns.getShort("COLUMN_NAME") + " <--> local: " + schema.get(i).getAttributeName());
				return false;
			}
			i++;
		}
		return true;
	}

	@Override
	public void dropTable(String tablename) throws SQLException {
		Statement stmt;
		assertConnection();
		stmt = this.connection.createStatement();
		stmt.executeUpdate("DROP TABLE " + tablename);

	}

	@Override
	public void truncateTable(String tablename) throws SQLException {
		Statement stmt;
		assertConnection();
		stmt = this.connection.createStatement();
		stmt.executeUpdate("TRUNCATE TABLE " + tablename);

	}

	@Override
	public Map<String, String> getInformation() throws SQLException {
		Map<String, String> infos = new TreeMap<String, String>();
		assertConnection();
		DatabaseMetaData dmd = this.connection.getMetaData();
		infos.put("Database Product Name", dmd.getDatabaseProductName());
		infos.put("Database Product Version", dmd.getDatabaseProductVersion());
		infos.put("Driver Name", dmd.getDriverName());
		infos.put("Driver Version", dmd.getDriverVersion());
		infos.put("URL", dmd.getURL());
		infos.put("Username", dmd.getUserName());

		return infos;
	}

	@Override
	public List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();
		assertConnection();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet res = meta.getTables(null, null, null, new String[] { "TABLE" });
		while (res.next()) {
			tables.add(res.getString("TABLE_NAME"));
		}

		return tables;
	}

	@Override
	public List<String> getSchemas() throws SQLException {
		List<String> tables = new ArrayList<String>();
		assertConnection();
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet res = meta.getSchemas();
		while (res.next()) {
			tables.add(res.getString("TABLE_SCHEM"));
		}
		return tables;
	}

	private void assertConnection() throws SQLException {
		if (!this.connected) {
			this.connection = DriverManager.getConnection(connString, connectionProps);
			this.connected = true;
		}

	}

	@Override
	public void checkProperties() throws SQLException {
		Connection con = DriverManager.getConnection(connString, connectionProps);
		con.close();
	}
}
