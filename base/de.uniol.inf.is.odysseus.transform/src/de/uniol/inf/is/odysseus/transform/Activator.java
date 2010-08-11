package de.uniol.inf.is.odysseus.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.RuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rules.TAccessAOExistsRule;
import de.uniol.inf.is.odysseus.transform.rules.TCreateMetadataRule;
import de.uniol.inf.is.odysseus.transform.rules.TDeleteRenameAORule;
import de.uniol.inf.is.odysseus.transform.rules.TSelectAORule;
import de.uniol.inf.is.odysseus.transform.rules.TSplitAORule;
import de.uniol.inf.is.odysseus.transform.rules.TTransformViewRule;

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
		
		// init rule flow (order is important)
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.INIT);
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.CREATE_METADATA);
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRuleFlowGroup(RuleFlowGroup.CLEANUP);	
		
		//loading default rules		
		TransformationInventory.getInstance().addRule(new TAccessAOExistsRule(), RuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TCreateMetadataRule(), RuleFlowGroup.CREATE_METADATA);
		TransformationInventory.getInstance().addRule(new TDeleteRenameAORule(), RuleFlowGroup.INIT);
		TransformationInventory.getInstance().addRule(new TSelectAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSplitAORule(), RuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TTransformViewRule(), RuleFlowGroup.ACCESS);
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
