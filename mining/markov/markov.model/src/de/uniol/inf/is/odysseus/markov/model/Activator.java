package de.uniol.inf.is.odysseus.markov.model;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.markov.model.algorithm.ForwardAlgorithm;
import de.uniol.inf.is.odysseus.markov.model.algorithm.MarkovAlgorithmDictionary;
import de.uniol.inf.is.odysseus.markov.model.algorithm.ViterbiAlgorithm;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		MarkovAlgorithmDictionary.getInstance().addAlgorithm("viterbi", ViterbiAlgorithm.class);
		MarkovAlgorithmDictionary.getInstance().addAlgorithm("forward", ForwardAlgorithm.class);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
