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

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;

/**
 * @author Dennis Geesen Created at: 08.11.2011
 */
public class DatatypeRegistry {

	private static HashMap<SDFDatatype, IDataTypeMappingHandler<?>> sdfdataHandlers = new HashMap<SDFDatatype, IDataTypeMappingHandler<?>>();
	private static HashMap<Integer, IDataTypeMappingHandler<?>> sqldataHandlers = new HashMap<Integer, IDataTypeMappingHandler<?>>();

	/**
	 * Registers the handler for all supported SQL and SDF data types
	 * @param handler the handler to register
	 */
	public static void registerDataHandler(IDataTypeMappingHandler<?> handler) {		
		String errMsg = "";
		// first, check all sdf types
		for (SDFDatatype type : handler.getSupportedSDFDataTypes()) {
			if (sdfdataHandlers.containsKey(type)) {
				errMsg += "Data Mapping handler for SDF-DataType " + type + " already registered.\n";
			}

		}
		// then check sql types
		for (int type : handler.getSupportedSQLDataTypes()) {
			if (sqldataHandlers.containsKey(type)) {				
				errMsg += "Data Mapping handler for SQL-DataType " + type + " already registered.\n";
			}
		}

		// something already exists...
		if (errMsg != "") {
			errMsg = "Adding of handler: "+handler.getClass().getCanonicalName()+" failed\n"+errMsg;
			throw new IllegalArgumentException(errMsg);
		}
		// all ok, put new handler
		for (SDFDatatype type : handler.getSupportedSDFDataTypes()) {
			sdfdataHandlers.put(type, handler);
		}

		for (int type : handler.getSupportedSQLDataTypes()) {
			sqldataHandlers.put(type, handler);
		}
	}

	/**
	 * Removes the handler and all supported data types 
	 * @param handler the handler to remove
	 */
	public static void removeDataHandler(IDataTypeMappingHandler<?> handler) {
		for (SDFDatatype type : handler.getSupportedSDFDataTypes()) {
			sdfdataHandlers.remove(type);
		}

		for (int type : handler.getSupportedSQLDataTypes()) {
			sqldataHandlers.remove(type);
		}
	}

	/**
	 * Gets the handler for a certain SDF data type
	 * @param dataType a SDF data type
	 * @return the handler for dataType
	 */
	public static IDataTypeMappingHandler<?> getDataHandler(SDFDatatype dataType) {
		return sdfdataHandlers.get(dataType);
	}

	/**
	 * Gets the handler for a certain SQL data type
	 * @param sqltype a SQL data type from {@link java.sql.Types}
	 * @return a data handler for sqltype
	 */
	public static IDataTypeMappingHandler<?> getDataHandler(int sqltype) {
		return sqldataHandlers.get(sqltype);
	}
	
	/**
	 * Gets the default SDF data type for a SQL data type
	 * @param sqltype the SQL type to look for
	 * @return the dedicated SDF data type
	 */
	public static SDFDatatype getSDFDatatype(int sqltype) {
		return getDataHandler(sqltype).getDefaultSDFDatatype();
	}
	
	/**
	 * Gets the default SQL data type for a SDF data type
	 * @param sdfdatatype the SDF data type to look for
	 * @return the dedicated SQL type from {@link java.sql.Types}
	 */
	public static int getSQLDatatype(SDFDatatype sdfdatatype){
		return getDataHandler(sdfdatatype).getDefaultSQLDatatype();
	}

	/**
	 * Checks whether the mapping between a SDF and SQL data type exists
	 * @param dt the SDF data type
	 * @param dbType the SQL data type from {@link java.sql.Types}
	 * @return
	 */
	public static boolean mappingExists(SDFDatatype dt, int dbType) {
		return getDataHandler(dbType).getSupportedSDFDataTypes().contains(dt);		
	}

}
