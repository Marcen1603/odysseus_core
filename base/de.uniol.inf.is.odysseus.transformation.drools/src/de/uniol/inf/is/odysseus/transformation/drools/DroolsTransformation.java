package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.ArrayList;
import java.util.List;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.agent.RuleAgent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.drools.RuleAgentFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

/**
 * @author Jonas Jacobi
 */
public class DroolsTransformation implements ITransformation {

	private static final String RULE_PATH = "/resources/transformation/rules";
	private static final String LOGGER_NAME = "transformation";
	private RuleBase rulebase;
	private static Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

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

	@SuppressWarnings("unused")
	private void activate(ComponentContext context) {
		BundleContext bundleContext = context.getBundleContext();
		bundleContext.addBundleListener(new BundleListener() {

			@Override
			public void bundleChanged(BundleEvent event) {
				System.out.println(event.getType() + ": " + event.getBundle());
			}
		});
		RuleAgent ra = RuleAgentFactory.createRuleAgent(bundleContext,
				RULE_PATH, LOGGER_NAME);
		this.rulebase = ra.getRuleBase();

	}

	@Override
	public IPhysicalOperator transform(ILogicalOperator op,
			TransformationConfiguration config) throws TransformationException {
		StatefulSession session = rulebase.newStatefulSession();
		session.insert(config);
		TopAO top = new TopAO();
		top.setInputAO(0, op);

		session.insert(config);
		addLogicalOperatorToSession(session, top);
		if (logger.isInfoEnabled()) {
			logger.info("transformation of: "
					+ AbstractTreeWalker.prefixWalk(op,
							new AlgebraPlanToStringVisitor()));
		}

		session.insert(this);
		session.startProcess("flow");
		session.fireAllRules();

		IPhysicalOperator physicalPO = top.getPhysInputPO(0);
		if (physicalPO == null) {
			List<ILogicalOperator> errors = new ArrayList<ILogicalOperator>();
			session.setGlobal("untranslatedOperators", errors);
			session.startProcess("collect_errors");
			session.fireAllRules();
			session.dispose();
			StringBuilder builder = new StringBuilder();
			int pos = 0;
			for (ILogicalOperator errOp : errors) {
				if (++pos > 1) {
					builder.append(", ");
				}
				builder.append(errOp.toString());
			}
			throw new TransformationException(config, errors);
		}
		session.dispose();
		if (logger.isInfoEnabled()) {
			logger.info("transformation result: info not yet implemented: "
					+ physicalPO);
		}
		return physicalPO;
	}
}
