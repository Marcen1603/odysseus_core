package de.uniol.inf.is.odysseus.priority;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;

public class Activator implements BundleActivator {


	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(Priority.class, IPriority.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeMetadataType(IPriority.class);
	}

}
