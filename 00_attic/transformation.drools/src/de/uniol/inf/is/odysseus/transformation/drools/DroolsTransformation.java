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
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.transformation.drools.debug.DebugEventListener;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.FindQueryRootsVisitor;
import de.uniol.inf.is.odysseus.util.LoggerHelper;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

/**
 * @author Jonas Jacobi
 */
public class DroolsTransformation implements ITransformation {

	private static final String RULE_PATH = "/resources/transformation/rules";
	private static final String LOGGER_NAME = "transformation";
	private static final boolean SHOW_ADDITIONAL_LOGGINGS = false;
	
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

	/**
	 * The plan can have more than one root, so more than
	 * one root will be returned. However, the physical root
	 * transformed of the logical operator passed to this methode
	 * will be first in the list.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op,
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
		//Debugging stuff		
		DebugEventListener debugEventListener = new DebugEventListener(LOGGER_NAME, SHOW_ADDITIONAL_LOGGINGS);
		session.addEventListener(debugEventListener.getAgendaEventListener());
		session.addEventListener(debugEventListener.getWorkingMemoryEventListener());
		session.addEventListener(debugEventListener.getRuleFlowEventListener());		
		 
		try {
			session.fireAllRules();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		IPhysicalOperator physicalPO = null;
		ArrayList<IPhysicalOperator> queryRoots = null;
		try {
			physicalPO = top.getPhysicalInput();
			
			// The physical plan can have more than one
			// root. So find all roots in the physical plan
			// that have no owner. These roots belong to the
			// current query.
			FindQueryRootsVisitor visitor = new FindQueryRootsVisitor<IPhysicalOperator>();
			AbstractGraphWalker walker = new AbstractGraphWalker();
			
			// since we pass physicalPO to the walker, we can
			// guarantee that the physical po will be the first
			// operator in the list. The walker will check
			// each node, whether it is a root or not. This
			// will be done in order of visiting the nodes.
			// physicalPO is the first node visited
			walker.prefixWalkPhysical(physicalPO, visitor);
			queryRoots = visitor.getResult(); 
			
		} catch (NoSuchElementException e) {
			addLogicalOperatorToSession(session, top, list);
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
		
		
//		return physicalPO;
		return queryRoots;
	}

	
	@SuppressWarnings( "unused")
	private String planToStringOLD(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		builder.append(physicalPO);
		builder.append('\n');
		if (physicalPO.isSink()) {
			for (PhysicalSubscription<?> sub : ((ISink<?>) physicalPO)
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
