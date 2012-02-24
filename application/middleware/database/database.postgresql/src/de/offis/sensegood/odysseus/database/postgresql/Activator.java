package de.offis.sensegood.odysseus.database.postgresql;

import java.sql.Types;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.offis.sensegood.odysseus.database.postgresql.drivers.PostgreSQLConnectionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;

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
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.ARRAY, SDFDatatype.OBJECT);
		DatabaseConnectionDictionary.getInstance().addFactory("postgresql", new PostgreSQLConnectionFactory());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
