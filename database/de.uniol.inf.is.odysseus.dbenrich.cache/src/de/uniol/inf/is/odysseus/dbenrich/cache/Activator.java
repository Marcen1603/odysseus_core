package de.uniol.inf.is.odysseus.dbenrich.cache;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.FIFO;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.LFU;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.LRU;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.Random;
import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.RemovalStrategyRegistry;

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
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		RemovalStrategyRegistry.register(new Random());
		RemovalStrategyRegistry.register(new FIFO());
		RemovalStrategyRegistry.register(new LRU());
		RemovalStrategyRegistry.register(new LFU());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
