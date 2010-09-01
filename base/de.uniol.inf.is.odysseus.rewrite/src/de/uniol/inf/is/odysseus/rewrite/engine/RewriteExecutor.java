package de.uniol.inf.is.odysseus.rewrite.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IRewrite;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

public class RewriteExecutor implements IRewrite {

	private static final String LOGGER_NAME = "rewrite";

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan) {
		RewriteConfiguration conf = new RewriteConfiguration(new HashSet<String>());
		return rewritePlan(plan, conf);
	}

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan, Set<String> rulesToApply) {
		RewriteConfiguration conf = new RewriteConfiguration(new HashSet<String>());
		return rewritePlan(plan, conf);
	}

	private ILogicalOperator rewritePlan(ILogicalOperator plan, RewriteConfiguration conf) {

		RewriteInventory rewriteInventory = new RewriteInventory(RewriteInventory.getInstance());
		RewriteEnvironment env = new RewriteEnvironment(conf, rewriteInventory);
		TopAO top = new TopAO();
		plan.subscribeSink(top, 0, 0, plan.getOutputSchema());

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperator(top, list, env);
		// *******
		PrintGraphVisitor<ILogicalOperator> pv = new PrintGraphVisitor<ILogicalOperator>();
		new AbstractGraphWalker<String, ILogicalOperator, LogicalSubscription>().prefixWalk(top, pv);
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.DEBUG, "Pre rewrite: \n" + pv.getResult());
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.DEBUG, "Processing rules...");
		// start transformation
		env.processEnvironment();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules done.");
		LogicalSubscription sub = top.getSubscribedToSource(0);
		ILogicalOperator ret = sub.getTarget();
		top.unsubscribeFromSource(ret, sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());

		pv = new PrintGraphVisitor<ILogicalOperator>();
		new AbstractGraphWalker<String, ILogicalOperator, LogicalSubscription>().prefixWalk(ret, pv);
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Post rewrite: " + pv.getResult());

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

}
