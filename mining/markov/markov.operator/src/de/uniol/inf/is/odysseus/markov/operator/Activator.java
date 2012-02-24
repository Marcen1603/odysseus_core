package de.uniol.inf.is.odysseus.markov.operator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.markov.operator.logical.MarkovAOBuilder;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static final String MARKOV = "MARKOV";

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		OperatorBuilderFactory.putOperatorBuilderType(MARKOV, new MarkovAOBuilder());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		OperatorBuilderFactory.removeOperatorBuilderType(MARKOV);
	}

}
