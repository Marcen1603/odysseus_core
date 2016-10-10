package de.uniol.inf.is.odysseus.timeseries;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.timeseries.imputation.IImputation;
import de.uniol.inf.is.odysseus.timeseries.imputation.ImputationRegistry;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public void addImputation(IImputation<Tuple<ITimeInterval>, ITimeInterval> imputationStrategy) {
		ImputationRegistry.getInstance().addImputation(imputationStrategy);
	}

	public void removeImputation(IImputation<Tuple<ITimeInterval>, ITimeInterval> imputationStrategy) {
		ImputationRegistry.getInstance().removeImputation(imputationStrategy);
	}

}
