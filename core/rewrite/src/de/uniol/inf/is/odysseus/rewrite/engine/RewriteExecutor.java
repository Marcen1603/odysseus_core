package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.planmanagement.IRewrite;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.util.SimplePlanPrinter;

public class RewriteExecutor implements IRewrite {

	private static final String LOGGER_NAME = "rewrite";

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan) {
		return rewritePlan(plan, (Set<String>)null);
	}

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan, Set<String> rulesToApply) {
		RewriteConfiguration conf = new RewriteConfiguration(new HashSet<String>());
		if (rulesToApply != null) throw new RuntimeException("Rules to use currently not supported");
		return rewritePlan(plan, conf);
	}

	private ILogicalOperator rewritePlan(ILogicalOperator plan, RewriteConfiguration conf) {
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Starting rewriting...");
		RewriteInventory rewriteInventory = new RewriteInventory(RewriteInventory.getInstance());
		RewriteEnvironment env = new RewriteEnvironment(conf, rewriteInventory);
		TopAO top = new TopAO();
		plan.subscribeSink(top, 0, 0, plan.getOutputSchema());

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperator(top, list, env);
		// *******
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Before rewriting: \n"+planPrinter.createString(plan));
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules...");
		// start transformation
		env.processEnvironment();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules done.");
		LogicalSubscription sub = top.getSubscribedToSource(0);
		ILogicalOperator ret = sub.getTarget();
		top.unsubscribeFromSource(ret, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());

		planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "After rewriting: \n"+planPrinter.createString(ret));
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Rewriting finished.");
		return ret;
	}

	private void addLogicalOperator(ILogicalOperator op, List<ILogicalOperator> inserted, RewriteEnvironment env) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			env.getWorkingMemory().insertObject(op);
			inserted.add(op);

			for (LogicalSubscription sub : op.getSubscribedToSource()) {
				addLogicalOperator(sub.getTarget(), inserted, env);
			}
			for (LogicalSubscription sub : op.getSubscriptions()) {
				addLogicalOperator(sub.getTarget(), inserted, env);
			}
		}
	}
	
	public void addRuleProvider(IRewriteRuleProvider provider){		
		RewriteInventory.getInstance().bindRuleProvider(provider);
	}
	
	public void removeRuleProvider(IRewriteRuleProvider provider){		
		RewriteInventory.getInstance().unbindRuleProvider(provider);
	}

}
