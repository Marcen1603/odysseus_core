package de.uniol.inf.is.odysseus.transformation.drools;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.agent.RuleAgent;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.drools.RuleAgentFactory;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;
import de.uniol.inf.is.odysseus.util.LoggerHelper;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

/**
 * @author Jonas Jacobi
 */
public class DroolsTransformation implements ITransformation {

	private static final String RULE_PATH = "/resources/transformation/rules";
	private static final String LOGGER_NAME = "transformation";
	private RuleBase rulebase;

	private static void addLogicalOperatorToSession(StatefulSession session,
			ILogicalOperator op, List<ILogicalOperator> inserted) {
		if (op == null) {
			return;
		}

		if (!inserted.contains(op)) {
			LoggerHelper.getInstance(LOGGER_NAME)
					.debug("insert into wm: " + op);
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

	protected void activate(ComponentContext context) {
		try {
			BundleContext bundleContext = context.getBundleContext();
			RuleAgent ra = RuleAgentFactory.createRuleAgent(bundleContext,
					RULE_PATH, LOGGER_NAME);
			this.rulebase = ra.getRuleBase();
		} catch (Throwable t) {
			LoggerHelper.getInstance(LOGGER_NAME).error(t.getMessage());
			throw new RuntimeException(t);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPhysicalOperator transform(ILogicalOperator op,
			TransformationConfiguration config) throws TransformationException {
		StatefulSession session = rulebase.newStatefulSession();
		session.insert(config);
		TopAO top = new TopAO();
		op.subscribeSink(top, 0, 0, op.getOutputSchema());

		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		addLogicalOperatorToSession(session, top, list);

		if (LoggerHelper.getInstance(LOGGER_NAME).isInfoEnabled()) {
//			LoggerHelper.getInstance(LOGGER_NAME).debug(
//					"transformation of: "
//							+ AbstractTreeWalker.prefixWalk(top,
//									new AlgebraPlanToStringVisitor()));
			
			// plans can be cyclic graphs now, so use our new
			// walker
			PrintGraphVisitor<ILogicalOperator> pv = new PrintGraphVisitor<ILogicalOperator>();
			new AbstractGraphWalker().prefixWalk(top, pv);
			LoggerHelper.getInstance(LOGGER_NAME).debug("transformation of: " + pv.getResult());
			
			LoggerHelper.getInstance(LOGGER_NAME).debug(
					"added to working memory " + list);
		}

		session.insert(this);
		session.startProcess("flow");

//		 WorkingMemoryConsoleLogger lg = new
//		 WorkingMemoryConsoleLogger(session);
//		 lg.clearFilters();
//		 session.addEventListener( new DebugAgendaEventListener() );
//		 session.addEventListener( new DebugWorkingMemoryEventListener() );
		
		try {
			session.fireAllRules();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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
		if (LoggerHelper.getInstance(LOGGER_NAME).isInfoEnabled()) {
//			LoggerHelper.getInstance(LOGGER_NAME).debug(
//					"transformation result: \n" + planToString(physicalPO, ""));
			AbstractGraphWalker walker = new AbstractGraphWalker();
			PrintGraphVisitor printGraph = new PrintGraphVisitor();
			walker.prefixWalkPhysical(physicalPO, printGraph);
			
			LoggerHelper.getInstance(LOGGER_NAME).debug("transformation result: \n" + printGraph.getResult());
		}		
		op.unsubscribeSink(top, 0, 0, op.getOutputSchema());
		return physicalPO;
	}

	
	@SuppressWarnings({ "unused", "unchecked" })
	private String planToStringOLD(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		builder.append(physicalPO);
		builder.append('\n');
		if (physicalPO.isSink()) {
			for (PhysicalSubscription sub : ((ISink<?>) physicalPO)
					.getSubscribedToSource()) {
				builder.append(planToString(
						(IPhysicalOperator) sub.getTarget(), "  " + indent));
			}
		}
		return builder.toString();
	}

	private String planToString(IPhysicalOperator physicalPO, String indent) {
		return this.planToString(physicalPO, indent,
				new ArrayList<IPhysicalOperator>());
	}

	private String planToString(IPhysicalOperator physicalPO, String indent,
			List<IPhysicalOperator> visited) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		if (!contains(visited,physicalPO)) {
			visited.add(physicalPO);
			builder.append(physicalPO);
			builder.append('\n');
			if (physicalPO.isSink()) {
				for (PhysicalSubscription<?> sub : ((ISink<?>) physicalPO)
						.getSubscribedToSource()) {
					builder.append(planToString((IPhysicalOperator) sub
							.getTarget(), "  " + indent, visited));
				}
			}
		}else{
			builder.append(physicalPO);
			builder.append('\n');
			builder.append(indent+"  [see above for following operators]\n");
		}
		return builder.toString();
	}
	
	private boolean contains(List<IPhysicalOperator> list, IPhysicalOperator op){
		for(IPhysicalOperator other : list){
			if(op==other){
				return true;
			}
		}
		return false;
	}

}
