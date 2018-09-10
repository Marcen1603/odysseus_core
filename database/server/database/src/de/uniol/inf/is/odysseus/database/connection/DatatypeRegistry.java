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

import java.sql.Types;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.BooleanDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.ByteDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.CharDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.DoubleDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.FloatDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IntegerDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.LongDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.ShortDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.StringDataTypeMappingHandler;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.TimestampDataTypeMappingHandler;

/**
 * @author Dennis Geesen Created at: 08.11.2011
 * @author Marco Grawunder
 */
@SuppressWarnings("rawtypes")
public class DatatypeRegistry {

	private static HashMap<SDFDatatype, Pair<Integer, IDataTypeMappingHandler>> sdfdataHandlers = new HashMap<>();
	private static HashMap<Integer, Pair<SDFDatatype, IDataTypeMappingHandler>> sqldataHandlers = new HashMap<>();

	private static HashMap<Class<?>, IDataTypeMappingHandler<?>> handlers = new HashMap<>();
	static {
		addMapping(SDFDatatype.BOOLEAN, Types.BOOLEAN,
				BooleanDataTypeMappingHandler.class, true);
		addMapping(Types.BIT, SDFDatatype.BOOLEAN,
				BooleanDataTypeMappingHandler.class);
		addMapping(SDFDatatype.BYTE, Types.TINYINT,
				ByteDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.DOUBLE, Types.DOUBLE,
				DoubleDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.CHAR, Types.CHAR,
				CharDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.FLOAT, Types.REAL,
				FloatDataTypeMappingHandler.class, true);
		addMapping(Types.FLOAT, SDFDatatype.FLOAT,
				FloatDataTypeMappingHandler.class);
		addMapping(SDFDatatype.INTEGER, Types.INTEGER,
				IntegerDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.UNSIGNEDINT16, Types.INTEGER,
				IntegerDataTypeMappingHandler.class, false);
		addMapping(Types.SMALLINT, SDFDatatype.INTEGER,
				IntegerDataTypeMappingHandler.class);
		addMapping(SDFDatatype.LONG, Types.BIGINT,
				LongDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.SHORT, Types.SMALLINT,
				ShortDataTypeMappingHandler.class, false);
		addMapping(SDFDatatype.STRING, Types.VARCHAR,
				StringDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.START_TIMESTAMP_STRING, Types.VARCHAR,
				StringDataTypeMappingHandler.class, false);
		addMapping(SDFDatatype.END_TIMESTAMP_STRING, Types.VARCHAR,
				StringDataTypeMappingHandler.class, false);
		addMapping(Types.LONGVARCHAR, SDFDatatype.STRING,
				StringDataTypeMappingHandler.class);
		addMapping(Types.LONGNVARCHAR, SDFDatatype.STRING,
				StringDataTypeMappingHandler.class);
		addMapping(Types.NCHAR, SDFDatatype.STRING,
				StringDataTypeMappingHandler.class);
		addMapping(Types.NVARCHAR, SDFDatatype.STRING,
				StringDataTypeMappingHandler.class);
		addMapping(SDFDatatype.TIMESTAMP, Types.TIMESTAMP,
				TimestampDataTypeMappingHandler.class, true);
		addMapping(SDFDatatype.POINT_IN_TIME, Types.TIMESTAMP,
				TimestampDataTypeMappingHandler.class, false);
		addMapping(SDFDatatype.START_TIMESTAMP, Types.TIMESTAMP,
				TimestampDataTypeMappingHandler.class, false);
		addMapping(SDFDatatype.END_TIMESTAMP, Types.TIMESTAMP,
				TimestampDataTypeMappingHandler.class, false);
		addMapping(SDFDatatype.DATE, Types.TIMESTAMP,
				TimestampDataTypeMappingHandler.class, false);
	}

	/**
	 * Gets the handler for a certain SDF data type
	 * 
	 * @param dataType
	 *            a SDF data type
	 * @return the handler for dataType
	 */
	public static IDataTypeMappingHandler getDataHandler(SDFDatatype dataType) {
		Pair<Integer, IDataTypeMappingHandler> handler = sdfdataHandlers.get(dataType);
		IDataTypeMappingHandler ret = null; 
		if (handler != null){
			ret = handler.getE2();
		}
		if (ret == null) {
			throw new IllegalArgumentException("No mapping for Odysseus type "
					+ dataType + " defined!");
		}
		return ret;
	}

	/**
	 * Gets the SQL data type for a SDF data type
	 * 
	 * @param sdfdatatype
	 *            the SDF data type to look for
	 * @return the dedicated SQL type from {@link java.sql.Types}
	 */
	public static int getSQLDatatype(SDFDatatype dataType) {
		Integer ret = sdfdataHandlers.get(dataType).getE1();
		if (ret == null) {
			throw new IllegalArgumentException("No mapping for Odysseus type "
					+ dataType + " defined!");
		}
		return ret;
	}

	/**
	 * Gets the handler for a certain SQL data type
	 * 
	 * @param sqltype
	 *            a SQL data type from {@link java.sql.Types}
	 * @return a data handler for sqltype
	 */
	public static IDataTypeMappingHandler<?> getDataHandler(int sqltype) {
		IDataTypeMappingHandler ret = sqldataHandlers.get(sqltype).getE2();
		if (ret == null) {
			throw new IllegalArgumentException("No mapping for SQL type "
					+ sqltype + " defined!");
		}
		return ret;
	}

	/**
	 * Gets the SDF data type for a SQL data type
	 * 
	 * @param sqltype
	 *            the SQL type to look for
	 * @return the dedicated SDF data type
	 */
	public static SDFDatatype getSDFDatatype(int sqltype) {
		SDFDatatype ret = sqldataHandlers.get(sqltype).getE1();
		if (ret == null) {
			throw new IllegalArgumentException("No mapping for SQL type "
					+ sqltype + " defined!");
		}
		return ret;
	}

	static private void addMapping(int b, SDFDatatype c, Class<?> handlerClass) {
		IDataTypeMappingHandler handler = getHandler(handlerClass);
		if (sqldataHandlers.containsKey(b)) {
			throw new IllegalArgumentException("Ambigious mapping definition! "
					+ b + " ist already mapped to "
					+ sqldataHandlers.get(b).getE1());
		}
		sqldataHandlers.put(new Integer(b), new Pair<>(c, handler));
	}

	static private void addMapping(SDFDatatype b, int c, Class<?> handlerClass,
			boolean isSymetric) {
		IDataTypeMappingHandler handler = getHandler(handlerClass);
		if (sdfdataHandlers.containsKey(b)) {
			throw new IllegalArgumentException("Ambigious mapping definition! "
					+ b + " ist already mapped to "
					+ sdfdataHandlers.get(b).getE1());
		}
		sdfdataHandlers.put(b, new Pair<>(c, handler));
		if (isSymetric) {
			addMapping(c, b, handlerClass);
		}
	}

	static private IDataTypeMappingHandler<?> getHandler(Class<?> handlerClass) {
		IDataTypeMappingHandler<?> handler = handlers.get(handlerClass);
		if (handler == null) {
			try {
				handler = (IDataTypeMappingHandler<?>) handlerClass
						.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			handlers.put(handlerClass, handler);
		}
		return handler;
	}

	/**
	 * Checks whether the mapping between a SDF and SQL data type exists
	 * 
	 * @param dt
	 *            the SDF data type
	 * @param dbType
	 *            the SQL data type from {@link java.sql.Types}
	 * @return
	 */
	public static boolean mappingExists(SDFDatatype dt, int dbType) {
		Pair<Integer, IDataTypeMappingHandler> p = sdfdataHandlers.get(dt);
		return (p != null && p.getE1().equals(dbType));
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		DatatypeRegistry reg = new DatatypeRegistry();
	}
}
