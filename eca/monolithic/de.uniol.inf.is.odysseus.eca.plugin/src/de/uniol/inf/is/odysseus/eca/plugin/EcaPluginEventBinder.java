package de.uniol.inf.is.odysseus.eca.plugin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

/**
 * Bindet den ServerExecutor und AclEventGenerator
 * Wird beim Start von OSGi aufgerufen (xml-file)
 * 
 */
public class EcaPluginEventBinder implements BundleActivator {
	private static final Logger LOG = LoggerFactory.getLogger(EcaPluginEventBinder.class);

	private static IServerExecutor executor;
	
	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = (IServerExecutor) serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}


	@Override
	public void start(BundleContext context) throws Exception {
		LOG.debug("EcaPluginEventBinder - start");

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

	public static IServerExecutor getExecutor() {
		return executor;
	}
}
