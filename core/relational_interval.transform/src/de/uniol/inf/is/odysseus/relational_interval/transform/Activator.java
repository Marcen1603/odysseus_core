package de.uniol.inf.is.odysseus.relational_interval.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
//		TransformationInventory.getInstance().addRule(new TApplicationTimestampRule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TJoinTIPOInsertDataMergeRule(), TransformRuleFlowGroup.METAOBJECTS);
//		TransformationInventory.getInstance().addRule(new TRelationalSlidingElementWindowTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
