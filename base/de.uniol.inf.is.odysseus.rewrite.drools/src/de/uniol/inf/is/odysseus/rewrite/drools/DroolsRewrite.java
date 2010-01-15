package de.uniol.inf.is.odysseus.rewrite.drools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.agent.RuleAgent;
import org.drools.audit.WorkingMemoryConsoleLogger;
import org.drools.event.DebugAgendaEventListener;
import org.drools.event.DebugRuleFlowEventListener;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.rule.Package;
import org.drools.rule.Rule;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.drools.RuleAgentFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IRewrite;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * Logical plan rewriting service, based on drools. It loads rule files (*.drl)
 * from '/resources/rewrite/rules', so you can provide rules via Fragment
 * Bundles. It provides a default ruleflow with
 * '/resources/rewrite/rules/ruleflow.rf'. Failing to load rule files (e.g.
 * because of erroneous rules) will trigger a warning in the log.
 * 
 * @author Jonas Jacobi
 */
public class DroolsRewrite implements IRewrite {

	private static final String RULE_PATH = "/resources/rewrite/rules";
	private static final String LOGGER_NAME = "rewrite";
	public static final String PACKAGE_NAME = "de.uniol.inf.is.odysseus.rewrite.rules";
	public RuleBase rulebase;
	public ReadWriteLock ruleBaseLock = new ReentrantReadWriteLock();
	private static Logger logger = LoggerFactory.getLogger(LOGGER_NAME);
	private BundleContext bundleContext;
	public List<Rule> removedRules = new LinkedList<Rule>();
	private Set<String> ruleNames;

	public DroolsRewrite() {
	}

	public ILogicalOperator rewritePlanInternal(ILogicalOperator plan) {

		logger.info("Current Top subscriptions  "
				+ plan.getSubscriptions().toString());

		System.out.println("RULES: ");
		for(Package pkg : rulebase.getPackages()){
			System.out.println("PACKAGE: " + pkg.getName());
			for(Rule r : pkg.getRules()){
				System.out.println(r.getName()+ " : " + r.getRuleFlowGroup());
			}
		}
		StatefulSession session = rulebase.newStatefulSession();
		TopAO top = new TopAO();
		plan.subscribeSink(top, 0, 0, plan.getOutputSchema());

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperatorToSession(session, top, list);
		if (logger.isInfoEnabled()) {
			logger.info("pre rewrite: "
					+ AbstractTreeWalker.prefixWalk(top,
							new AlgebraPlanToStringVisitor()));
		}

//		WorkingMemoryConsoleLogger lg = new WorkingMemoryConsoleLogger(session);
//		lg.clearFilters();
//		session.addEventListener(new DebugAgendaEventListener());
//		session.addEventListener(new DebugWorkingMemoryEventListener());
		session.startProcess("RuleFlow");

		session.fireAllRules();
		session.dispose();
		if (logger.isInfoEnabled()) {
			logger.info("post rewrite:"
					+ AbstractTreeWalker.prefixWalk(top,
							new AlgebraPlanToStringVisitor()));

		}
		LogicalSubscription sub = top.getSubscribedToSource(0);
		ILogicalOperator ret = sub.getTarget();
		top.unsubscribeFromSource(ret, sub.getSinkPort(), sub
				.getSourcePort(), sub.getSchema());
		if (logger.isInfoEnabled()) {
			logger.info("post rewrite:"
					+ AbstractTreeWalker.prefixWalk(ret,
							new AlgebraPlanToStringVisitor()));

		}
		return ret;
	}

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan,
			Set<String> rulesToApply) {
		ruleBaseLock.writeLock().lock();
		try {
			Package pkg = this.rulebase.getPackage(PACKAGE_NAME);
			resetRules(pkg);
			for (Rule rule : pkg.getRules()) {
				if (!rulesToApply.contains(rule.getName())) {
					pkg.removeRule(rule);
					this.removedRules.add(rule);
				}
			}
			return rewritePlanInternal(plan);
		} finally {
			ruleBaseLock.writeLock().unlock();
		}
	}

	public void resetRules(Package pkg) {
		for (Rule rule : removedRules) {
			pkg.addRule(rule);
		}
		removedRules.clear();
	}

	public Set<String> getRules() {
		return this.ruleNames;
	}

	private static void addLogicalOperatorToSession(StatefulSession session,
			ILogicalOperator op, List<ILogicalOperator> inserted) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			logger.info("insert into wm: " + op);
			session.insert(op);
			inserted.add(op);

			for (LogicalSubscription sub : op.getSubscribedToSource()) {
				addLogicalOperatorToSession(session, sub.getTarget(), inserted);
			}
			for (LogicalSubscription sub : op.getSubscriptions()) {
				addLogicalOperatorToSession(session, sub.getTarget(), inserted);
			}
		}
	}

	public void activate(ComponentContext context) {
		try {
			this.bundleContext = context.getBundleContext();
			RuleAgent ra = RuleAgentFactory.createRuleAgent(this.bundleContext,
					RULE_PATH, LOGGER_NAME);
			this.ruleBaseLock.readLock().lock();
			try {
				this.rulebase = ra.getRuleBase();
				this.ruleNames = new HashSet<String>();
				Package pkg = this.rulebase.getPackage(PACKAGE_NAME);
				for (Rule rule : pkg.getRules()) {
					this.ruleNames.add(rule.getName());
				}
			} finally {
				this.ruleBaseLock.readLock().unlock();
			}
		} catch (Throwable t) {
			logger.error(t.getMessage());
			throw new RuntimeException(t);
		}
	}

	/*
	 * The locking mechanism allows multiple parallel calls to this method, but
	 * only one call at a time to rewritePlan(ILogicalOperator, Set<String>) as
	 * those use different rules and modify the singular rulebase.
	 */
	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan) {
		try {
			this.ruleBaseLock.writeLock().lock();
			try {
				Package pkg = this.rulebase.getPackage(PACKAGE_NAME);
				for (Rule rule : this.removedRules) {
					pkg.addRule(rule);
				}
				this.removedRules.clear();
			} finally {
				this.ruleBaseLock.readLock().lock();
				this.ruleBaseLock.writeLock().unlock();
			}
			return rewritePlanInternal(plan);
		} finally {
			this.ruleBaseLock.readLock().unlock();
		}
	}
}
