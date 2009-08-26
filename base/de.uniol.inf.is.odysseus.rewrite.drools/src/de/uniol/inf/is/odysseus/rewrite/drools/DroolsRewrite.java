package de.uniol.inf.is.odysseus.rewrite.drools;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.agent.RuleAgent;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.drools.RuleAgentFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IRewrite;
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
 * @todo TODO konfigurierbar machen, welche regeln ausgefuehrt werden sollen,
 *       evtl auch ueber ruleflows ..
 * @author Jonas Jacobi
 */
public class DroolsRewrite implements IRewrite {

	private static final String RULE_PATH = "/resources/rewrite/rules";
	private static final String LOGGER_NAME = "rewrite";
	private RuleBase rulebase;
	private static Logger logger = LoggerFactory.getLogger(LOGGER_NAME);
	private BundleContext bundleContext;

	public DroolsRewrite() {
	}

	@Override
	public ILogicalOperator rewritePlan(ILogicalOperator plan) {
		StatefulSession session = rulebase.newStatefulSession();
		TopAO top = new TopAO();
		top.setInputAO(0, plan);

		addLogicalOperatorToSession(session, top);
		if (logger.isInfoEnabled()) {
			logger.info("pre rewrite: "
					+ AbstractTreeWalker.prefixWalk(plan,
							new AlgebraPlanToStringVisitor()));
		}

		session.startProcess("RuleFlow");
		session.fireAllRules();
		session.dispose();
		if (logger.isInfoEnabled()) {
			logger.info("post rewrite:"
					+ AbstractTreeWalker.prefixWalk(top.getInputAO(),
							new AlgebraPlanToStringVisitor()));

		}
		return top.getInputAO();
	}

	private static void addLogicalOperatorToSession(StatefulSession session,
			ILogicalOperator op) {
		if (op == null) {
			return;
		}

		session.insert(op);
		for (int i = 0; i < op.getNumberOfInputs(); ++i) {
			addLogicalOperatorToSession(session, op.getInputAO(i));
		}
	}

	public void activate(ComponentContext context) {
		this.bundleContext = context.getBundleContext();
		RuleAgent ra = RuleAgentFactory.createRuleAgent(this.bundleContext,
				RULE_PATH, LOGGER_NAME);
		this.rulebase = ra.getRuleBase();
	}
}
