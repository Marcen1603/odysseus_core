package de.uniol.inf.is.odysseus.kdds.frequent;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.kdds.frequent.builder.FrequentItemAOBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static final String FREQUENT = "FREQUENT";
	
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		OperatorBuilderFactory.putOperatorBuilderType(FREQUENT, FrequentItemAOBuilder.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		OperatorBuilderFactory.removeOperatorBuilderType(FREQUENT);
	}

}
