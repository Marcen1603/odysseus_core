package de.uniol.inf.is.odysseus.relational.rewrite;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.relational.rewrite.rules.RDeleteProjectionWithoutFunctionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RDeleteSelectionWithoutPredicate;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RInitPredicatesRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RMergeSelectionJoinRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSplitSelectionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchProjectionRenameRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchProjectionWindowRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionJoinRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionProjectionRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionRenameRule;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RSwitchSelectionWindowRule;
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
		RewriteInventory.getInstance().addRule(new RDeleteProjectionWithoutFunctionRule(), RewriteRuleFlowGroup.DELETE);
		RewriteInventory.getInstance().addRule(new RDeleteSelectionWithoutPredicate(), RewriteRuleFlowGroup.DELETE);
		RewriteInventory.getInstance().addRule(new RInitPredicatesRule(), RewriteRuleFlowGroup.CLEANUP);
		RewriteInventory.getInstance().addRule(new RMergeSelectionJoinRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSplitSelectionRule(), RewriteRuleFlowGroup.SPLIT);
		RewriteInventory.getInstance().addRule(new RSwitchProjectionRenameRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSwitchProjectionWindowRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSwitchSelectionJoinRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSwitchSelectionProjectionRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSwitchSelectionRenameRule(), RewriteRuleFlowGroup.SWITCH);
		RewriteInventory.getInstance().addRule(new RSwitchSelectionWindowRule(), RewriteRuleFlowGroup.SWITCH);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
