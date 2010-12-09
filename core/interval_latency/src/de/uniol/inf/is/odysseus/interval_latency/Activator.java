package de.uniol.inf.is.odysseus.interval_latency;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;

public class Activator implements BundleActivator {

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(IntervalLatency.class, ITimeInterval.class, ILatency.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeCombinedMetadataType(ITimeInterval.class, ILatency.class);
	}

}
