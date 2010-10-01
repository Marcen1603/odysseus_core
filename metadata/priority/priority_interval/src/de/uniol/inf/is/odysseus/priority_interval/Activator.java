package de.uniol.inf.is.odysseus.priority_interval;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class Activator implements BundleActivator {

	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(IntervalPriority.class, ITimeInterval.class, IPriority.class );
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeCombinedMetadataType(IntervalPriority.class);
	}

}
