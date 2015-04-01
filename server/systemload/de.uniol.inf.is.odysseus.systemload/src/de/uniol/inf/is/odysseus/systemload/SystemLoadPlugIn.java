package de.uniol.inf.is.odysseus.systemload;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.systemload.impl.BufferedSigarWrapper;

public class SystemLoadPlugIn implements BundleActivator {

	public static final BufferedSigarWrapper SIGAR_WRAPPER = new BufferedSigarWrapper();

	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

}
