package de.uniol.inf.is.odysseus.mining;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;
import de.uniol.inf.is.odysseus.mining.metadata.IntervalLatencyMining;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
    @SuppressWarnings("unchecked")
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		MetadataRegistry.addMetadataType(IntervalLatencyMining.class, ITimeInterval.class, ILatency.class, IMiningMetadata.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    @SuppressWarnings("unchecked")
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		MetadataRegistry.removeCombinedMetadataType(ITimeInterval.class,
				ILatency.class, IMiningMetadata.class);
	}

}
