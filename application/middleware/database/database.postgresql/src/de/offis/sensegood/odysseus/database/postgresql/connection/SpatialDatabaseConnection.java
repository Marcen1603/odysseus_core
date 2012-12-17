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
package de.offis.sensegood.odysseus.database.postgresql.connection;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
public class SpatialDatabaseConnection extends DatabaseConnection {

	
	public SpatialDatabaseConnection(String str, Properties props) {
		super(str, props);
		try {
			((org.postgresql.PGConnection) getConnection()).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
			((org.postgresql.PGConnection) getConnection()).addDataType("box2d", Class.forName("org.postgis.PGbox2d"));
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createTable(String tablename, SDFSchema schema) {
		try {			
			Statement st = getConnection().createStatement();
			String table = "CREATE TABLE " + tablename + "(";
			String sep = "";
			for (SDFAttribute attribute : schema) {
				table = table + sep + attribute.getAttributeName() + " ";
				SDFDatatype d = attribute.getDatatype();
				if (d.getClass().getSimpleName().equals("SDFSpatialDatatype"))
					table = table + "GEOMETRY";
				else
					table = table + getDBMSSpecificType(DatatypeRegistry.getInstance().getJDBCDatatype(attribute.getDatatype()));
				sep = ", ";
			}
			table = table + ")";	
			st.executeUpdate(table);			
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	
}
