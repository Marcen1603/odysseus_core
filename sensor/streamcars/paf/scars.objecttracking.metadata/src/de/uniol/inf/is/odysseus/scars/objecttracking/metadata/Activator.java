package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		MetadataRegistry.addMetadataType(StreamCarsMetaData.class, 
				ITimeInterval.class, 
				IPredictionFunctionKey.class, 
				ILatency.class, 
				IProbability.class, 
				IApplicationTime.class, 
				IConnectionContainer.class,
				IGain.class);

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		MetadataRegistry.removeMetadataType(StreamCarsMetaData.class);
	}

}
