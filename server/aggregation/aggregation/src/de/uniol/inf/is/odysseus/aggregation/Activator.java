package de.uniol.inf.is.odysseus.aggregation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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
//		AggregationFunctionRegistry.getInstance().addFunction(new Avg<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new Count<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new DistinctNest<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new DistinctCount<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new Nest<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new Sum<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new TopK<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new Trigger<>());
//		AggregationFunctionRegistry.getInstance().addFunction(new MinMax<>(false));
//		AggregationFunctionRegistry.getInstance().addFunction(new MinMax<>(true));
//		AggregationFunctionRegistry.getInstance().addFunction(new Variance<>());
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
