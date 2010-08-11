package de.uniol.inf.is.odysseus.broker.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.RuleFlowGroup;

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
		TransformationInventory.getInstance().addRule(new TBrokerAccessAORule(), RuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TBrokerAOExistsRule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TBrokerAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TBrokerCycleDetectionRule(), RuleFlowGroup.CLEANUP);
		TransformationInventory.getInstance().addRule(new TBrokerJoinTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TMetricAORule(), RuleFlowGroup.TRANSFORMATION);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
