package de.uniol.inf.is.odysseus.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
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
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.INIT);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.CREATE_METADATA);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.CLEANUP);	
		
		//loading default rules		
		TransformationInventory.getInstance().addRule(new TAccessAOExistsRule(), TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TCreateMetadataRule(), TransformRuleFlowGroup.CREATE_METADATA);
		TransformationInventory.getInstance().addRule(new TDeleteRenameAORule(), TransformRuleFlowGroup.INIT);
		TransformationInventory.getInstance().addRule(new TSelectAORule(), TransformRuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TSplitAORule(), TransformRuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TTransformViewRule(), TransformRuleFlowGroup.ACCESS);
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
