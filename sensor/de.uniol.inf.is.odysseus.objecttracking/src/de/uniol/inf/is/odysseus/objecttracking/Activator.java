package de.uniol.inf.is.odysseus.objecttracking;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.base.MetadataRegistry;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IntervalProbabilityLatencyPrediction;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		MetadataRegistry.addMetadataType(IntervalProbabilityLatencyPrediction.class, ITimeInterval.class, IProbability.class, ILatency.class, IPredictionFunctionKey.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	public void stop(BundleContext context) throws Exception {
		MetadataRegistry.removeCombinedMetadataType(IntervalProbabilityLatencyPrediction.class);
	}

}
