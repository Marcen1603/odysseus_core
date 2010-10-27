package de.uniol.inf.is.odysseus.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rules.RuleProvider;

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
		
		// init rule flow (order is important)
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.INIT);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.CREATE_METADATA);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRuleFlowGroup(TransformRuleFlowGroup.CLEANUP);		
		
		//loading my own rules because self-binding-services don't work
		TransformationInventory.getInstance().bindRuleProvider(new RuleProvider());
		
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
