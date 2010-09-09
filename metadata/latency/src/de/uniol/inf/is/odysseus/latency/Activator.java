package de.uniol.inf.is.odysseus.latency;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;

public class Activator implements BundleActivator {

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(Latency.class, ILatency.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeMetadataType(ILatency.class);
	}

}
