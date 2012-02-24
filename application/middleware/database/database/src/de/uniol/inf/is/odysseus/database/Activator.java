package de.uniol.inf.is.odysseus.database;

import java.sql.Types;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.database.drivers.MySQLConnectionFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		DatabaseConnectionDictionary.getInstance().addFactory("mysql", new MySQLConnectionFactory());
		
		//Mappings Database -> Odysseus
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.ARRAY, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.BIGINT, SDFDatatype.LONG);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.BINARY, SDFDatatype.LONG);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.BIT, SDFDatatype.BOOLEAN);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.BLOB, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.BOOLEAN, SDFDatatype.BOOLEAN);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.CHAR, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.CLOB, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.DATALINK, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.DATE, SDFDatatype.DATE);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.DECIMAL, SDFDatatype.INTEGER);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.DISTINCT, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.DOUBLE, SDFDatatype.DOUBLE);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.FLOAT, SDFDatatype.FLOAT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.INTEGER, SDFDatatype.INTEGER);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.JAVA_OBJECT, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.LONGNVARCHAR, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.LONGVARBINARY, SDFDatatype.LONG);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.LONGVARCHAR, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.NCHAR, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.NCLOB, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.NULL, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.NUMERIC, SDFDatatype.DOUBLE);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.NVARCHAR, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.REAL, SDFDatatype.FLOAT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.REF, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.ROWID, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.SMALLINT, SDFDatatype.INTEGER);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.SQLXML, SDFDatatype.STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.STRUCT, SDFDatatype.OBJECT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.TIME, SDFDatatype.TIMESTAMP);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.TIMESTAMP, SDFDatatype.TIMESTAMP);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.TINYINT, SDFDatatype.INTEGER);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.VARBINARY, SDFDatatype.LONG);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.VARCHAR, SDFDatatype.STRING);
		
	
		//Mappings Odysseus -> DBMS
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.BOOLEAN, Types.BOOLEAN);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.BYTE, Types.BINARY);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.DATE, Types.DATE);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.DOUBLE, Types.DOUBLE);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.END_TIMESTAMP, Types.BIGINT);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.FLOAT, Types.FLOAT);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.INTEGER, Types.INTEGER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.LONG, Types.BIGINT);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.OBJECT, Types.JAVA_OBJECT);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.POINT_IN_TIME, Types.BIGINT);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.STRING, Types.VARCHAR);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.TIMESTAMP, Types.BIGINT);	
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFDatatype.START_TIMESTAMP, Types.BIGINT);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
