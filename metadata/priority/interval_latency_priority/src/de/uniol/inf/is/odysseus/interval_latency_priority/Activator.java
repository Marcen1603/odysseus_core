package de.uniol.inf.is.odysseus.interval_latency_priority;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class Activator implements BundleActivator {

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(IntervalLatencyPriority.class,
				ITimeInterval.class, ILatency.class, IPriority.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeCombinedMetadataType(ITimeInterval.class,
				ILatency.class, IPriority.class);
	}

}
