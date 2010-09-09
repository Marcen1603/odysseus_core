package de.uniol.inf.is.odysseus.rewrite;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rewrite.engine.RewriteInventory;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;

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
		
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.DELETE);
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.SPLIT);
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.GROUP);
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.DELETE);
		RewriteInventory.getInstance().addRuleFlowGroup(RewriteRuleFlowGroup.CLEANUP);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
