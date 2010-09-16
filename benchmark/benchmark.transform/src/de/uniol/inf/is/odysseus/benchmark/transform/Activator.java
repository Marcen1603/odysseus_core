package de.uniol.inf.is.odysseus.benchmark.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

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
//		TransformationInventory.getInstance().addRule(new TAlgebra2BenchmarkAORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TBatchProducerAORule(), TransformRuleFlowGroup.ACCESS);
//		TransformationInventory.getInstance().addRule(new TTestProducerAORule(), TransformRuleFlowGroup.ACCESS);
//		TransformationInventory.getInstance().addRule(new TBenchmarkAORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TBenchmarkBufferedPipeRule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TBenchmarkDirectInterlinkBufferRule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TBenchmarkOutofOrderBufferRule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TBenchmarkStrongOrderBufferRule(), TransformRuleFlowGroup.TRANSFORMATION);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
