package de.uniol.inf.is.odysseus.interval.transform;

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
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
//		TransformationInventory.getInstance().addRule(new TDifferenceAORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TExistenceAORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TJoinAORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TJoinAOSetSARule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TJoinTIPOAddMetadataMergeRule(), TransformRuleFlowGroup.METAOBJECTS);
//		TransformationInventory.getInstance().addRule(new TJoinTIPOSetMetadataMerge(), TransformRuleFlowGroup.METAOBJECTS);
//		TransformationInventory.getInstance().addRule(new TStreamGroupingWithAggregationTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TSystemTimestampRule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TUnionTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		
//		TransformationInventory.getInstance().addRule(new TSlidingAdvanceTimeWindowTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TSlidingElementWindowTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TSlidingPeriodicWindowTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TSlidingTimeWindowTIPORule(), TransformRuleFlowGroup.TRANSFORMATION);
//		TransformationInventory.getInstance().addRule(new TUnboundedWindowRule(), TransformRuleFlowGroup.TRANSFORMATION);
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
