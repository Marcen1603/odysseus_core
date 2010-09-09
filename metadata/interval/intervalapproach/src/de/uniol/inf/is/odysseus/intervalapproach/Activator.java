package de.uniol.inf.is.odysseus.intervalapproach;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;

public class Activator implements BundleActivator {

	@Override
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(TimeInterval.class,
				ITimeInterval.class);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeMetadataType(ITimeInterval.class);
	}

}
