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

package de.uniol.inf.is.odysseus.sink.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import  org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public abstract class AbstractDatabaseConnectionFactory implements IDatabaseConnectionFactory {

	
	private static Logger logger = LoggerFactory.getLogger(AbstractDatabaseConnectionFactory.class);
	protected abstract Map<SDFDatatype, String> getDatatypeMappings();

	protected String getSQLDatatype(SDFDatatype dt) {
		return getDatatypeMappings().get(dt);
	}

	protected static Properties getCredentials(String userName, String password) {
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		return connectionProps;
	}

	public boolean tableExists(Connection connection, String tablename) {
		try {
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

	public boolean equalSchemas(Connection connection, String tablename, SDFAttributeList schema) {
		try {
			DatabaseMetaData meta = connection.getMetaData();
			int i = 0;
			ResultSet rsColumns = meta.getColumns(null, null, tablename, null);						
			while (rsColumns.next()) {
				if(i>=schema.size()){
					// unterschiedliche anzahl
					return false;
				}				
				String dbType = rsColumns.getString("TYPE_NAME");
				String expectedType = getSQLDatatype(schema.get(i).getDatatype());
				
				if(!dbType.equalsIgnoreCase(expectedType)){
					logger.error("Expected types for stream and database are not equal for");
					logger.error("- database: "+dbType+" <--> local: "+expectedType);
					logger.error("- database: "+rsColumns.getShort("COLUMN_NAME")+" <--> local: "+schema.get(i).getAttributeName());
					return false;
				}
				i++;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

}
