package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.agent.RuleAgent;
//import org.drools.audit.WorkingMemoryConsoleLogger;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.drools.RuleAgentFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
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
	private static Logger _logger = null;

	// Dieses hier ist ein Hack damit das Drools-Plugin keine Fehler liefert!
	private void error(String message) {
		initLogger();
		if (_logger != null){
			_logger.error(message);
		}
	}
	
	private static void info(String info){
		initLogger();
		if (_logger != null){
			_logger.info(info);
		}
	}
	
	private static Logger initLogger(){
		try{
			_logger = LoggerFactory.getLogger(LOGGER_NAME);
			return _logger;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean isInfoEnabled() {
		initLogger();
		if (_logger != null){
			return _logger.isInfoEnabled();
		}else{
			return false;
		}
	}
	
	private static void addLogicalOperatorToSession(StatefulSession session,
			ILogicalOperator op, List<ILogicalOperator> inserted) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			info("insert into wm: "+op);
			session.insert(op);
			inserted.add(op);

			for (LogicalSubscription sub : op.getSubscribedTo()) {
				addLogicalOperatorToSession(session, sub.getTarget(), inserted);
			}
			for (LogicalSubscription sub : op.getSubscriptions()) {
				addLogicalOperatorToSession(session, sub.getTarget(), inserted);
			}
		}
	}

	protected void activate(ComponentContext context) {
		try {
			BundleContext bundleContext = context.getBundleContext();
			RuleAgent ra = RuleAgentFactory.createRuleAgent(bundleContext,
					RULE_PATH, LOGGER_NAME);
			this.rulebase = ra.getRuleBase();
		} catch (Throwable t) {
			error(t.getMessage());
			throw new RuntimeException(t);
		}
	}



	@Override
	public IPhysicalOperator transform(ILogicalOperator op,
			TransformationConfiguration config) throws TransformationException {
		StatefulSession session = rulebase.newStatefulSession();
		session.insert(config);
		TopAO top = new TopAO();
		op.subscribe(top, 0, 0, op.getOutputSchema());

		session.insert(config);
		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperatorToSession(session, top, list);

		if (isInfoEnabled()) {
			info("transformation of: "
					+ AbstractTreeWalker.prefixWalk(top,
							new AlgebraPlanToStringVisitor()));
			info("added to working memory "+list);
		}

		session.insert(this);
		session.startProcess("flow");

//		 WorkingMemoryConsoleLogger lg = new
//		 WorkingMemoryConsoleLogger(session);
//		 lg.clearFilters();

		session.fireAllRules();
		IPhysicalOperator physicalPO = null; 
		try {
			physicalPO = top.getPhysicalInput();
		} catch (NoSuchElementException e) {
			List<ILogicalOperator> errors = new ArrayList<ILogicalOperator>();
			session.setGlobal("untranslatedOperators", errors);
			session.startProcess("collect_errors");
			session.fireAllRules();
			session.dispose();
			throw new TransformationException(config, errors);
		}
		session.dispose();
		if (isInfoEnabled()) {
			info("transformation result: info not yet implemented: "
					+ physicalPO);
		}
		op.subscribe(top, 0, 0, op.getOutputSchema());
		return physicalPO;
	}


}
