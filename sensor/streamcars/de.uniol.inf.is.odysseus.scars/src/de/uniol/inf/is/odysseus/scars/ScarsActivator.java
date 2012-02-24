package de.uniol.inf.is.odysseus.scars;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.metadata.StreamCarsMetaData;

public class ScarsActivator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		ScarsActivator.context = bundleContext;
		
		MetadataRegistry.addMetadataType(StreamCarsMetaData.class,
				ITimeInterval.class,
				IPredictionFunctionKey.class,
				ILatency.class,
				IProbability.class,
				IApplicationTime.class,
				IConnectionContainer.class,
				IGain.class,
				IObjectTrackingLatency.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		ScarsActivator.context = null;
		MetadataRegistry.removeMetadataType(StreamCarsMetaData.class);
	}

}
