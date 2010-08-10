package de.uniol.inf.is.odysseus.transform.interval;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.RuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.interval.join.TJoinAORule;
import de.uniol.inf.is.odysseus.transform.interval.join.TJoinAOSetSARule;
import de.uniol.inf.is.odysseus.transform.interval.join.TJoinTIPOAddMetadataMergeRule;
import de.uniol.inf.is.odysseus.transform.interval.join.TJoinTIPOSetMetadataMerge;
import de.uniol.inf.is.odysseus.transform.interval.window.TSlidingAdvanceTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.transform.interval.window.TSlidingElementWindowTIPORule;
import de.uniol.inf.is.odysseus.transform.interval.window.TSlidingPeriodicWindowTIPORule;
import de.uniol.inf.is.odysseus.transform.interval.window.TSlidingTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.transform.interval.window.TUnboundedWindowRule;

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
		TransformationInventory.getInstance().addRule(new TDifferenceAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TExistenceAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TJoinAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TJoinAOSetSARule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TJoinTIPOAddMetadataMergeRule(), RuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRule(new TJoinTIPOSetMetadataMerge(), RuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRule(new TStreamGroupingWithAggregationTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSystemTimestampRule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TUnionTIPORule(), RuleFlowGroup.TRANSFORMATION);
		
		TransformationInventory.getInstance().addRule(new TSlidingAdvanceTimeWindowTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSlidingElementWindowTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSlidingPeriodicWindowTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSlidingTimeWindowTIPORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TUnboundedWindowRule(), RuleFlowGroup.TRANSFORMATION);
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
