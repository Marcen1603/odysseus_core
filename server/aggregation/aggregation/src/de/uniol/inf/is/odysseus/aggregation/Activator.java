package de.uniol.inf.is.odysseus.aggregation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.aggregation.functions.impl.Avg;
import de.uniol.inf.is.odysseus.aggregation.functions.impl.Count;
import de.uniol.inf.is.odysseus.aggregation.functions.impl.DistinctNest;
import de.uniol.inf.is.odysseus.aggregation.functions.impl.Nest;
import de.uniol.inf.is.odysseus.aggregation.functions.impl.Sum;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.builder.AggregationFunctionRegistry;

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
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		AggregationFunctionRegistry.getInstance().addFunction(new Avg<>());
		AggregationFunctionRegistry.getInstance().addFunction(new Count<>());
		AggregationFunctionRegistry.getInstance().addFunction(new DistinctNest<>());
		AggregationFunctionRegistry.getInstance().addFunction(new Nest<>());
		AggregationFunctionRegistry.getInstance().addFunction(new Sum<>());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
