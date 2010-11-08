package de.uniol.inf.is.odysseus.transform.engine;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.FindQueryRootsVisitor;
import de.uniol.inf.is.odysseus.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.util.SimplePlanPrinter;

/**
 * entry point for transformation (is bound by osgi-service)
 * 
 * @author DGeesen
 */
public class TransformationExecutor implements ITransformation {

	private static final String LOGGER_NAME = "transform";	

	public TransformationExecutor() {

	}

	@Override
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op, TransformationConfiguration config, User caller) throws TransformationException {		
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Starting transformation of " + op + "...");		
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Before transformation: \n"+planPrinter.createString(op));
		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		ArrayList<IPhysicalOperator> plan = new ArrayList<IPhysicalOperator>();
		TopAO top = new TopAO();
		op.subscribeSink(top, 0, 0, op.getOutputSchema());
		/** 
		 * creating a new transformation environment
		 * changes of inventory aren't considered!
		 * Otherwise a rule is able to work on different WM. 
		 * therefore: cloning instance by copy constructor! that means:
		 * Singleton = global state 
		 * concrete instance = local state for this instance
		 */
		

		TransformationInventory concreteTransformInvent = new TransformationInventory(TransformationInventory.getInstance());
		TransformationEnvironment env = new TransformationEnvironment(config, concreteTransformInvent, caller);

		addLogicalOperator(top, list, env);
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules...");
		// start transformation
		env.processEnvironment();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "Processing rules done.");

		IPhysicalOperator physicalPO = top.getPhysicalInput();
		if (physicalPO == null) {
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, "PhysicalInput of TopAO is null!");
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, "Current working memory:");
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, env.getWorkingMemory().getCurrentContent().toString());
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, "Further information: \n" + env.getWorkingMemory().getDebugTrace());
		}

		IGraphNodeVisitor<IPhysicalOperator, ArrayList<IPhysicalOperator>> visitor = new FindQueryRootsVisitor<IPhysicalOperator>();
		AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, ?> walker = new AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, LogicalSubscription>();
		walker.prefixWalkPhysical(physicalPO, visitor);
		plan = visitor.getResult();
		// Prefix Walker finds only roots that are not part of another query
		// physicalPO is in every case root of this query, so if not already found, add to plan 
		if (!plan.contains(physicalPO)){
			plan.add(physicalPO);
		}
		if(plan.isEmpty()){
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, "Plan is empty! If transformation was successful, it is possible that there are no root-operators!");
		}
		SimplePlanPrinter<IPhysicalOperator> physicalPlanPrinter = new SimplePlanPrinter<IPhysicalOperator>();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE, "After transformation: \n"+physicalPlanPrinter.createString(physicalPO));
		
		
		op.unsubscribeSink(top, 0, 0, op.getOutputSchema());
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO, "Transformation of " + op + " finished");
		return plan;
	}

	private void addLogicalOperator(ILogicalOperator op, List<ILogicalOperator> inserted, TransformationEnvironment env) {
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
	
	public void addRuleProvider(ITransformRuleProvider provider){		
		TransformationInventory.getInstance().bindRuleProvider(provider);
	}
	
	public void removeRuleProvider(ITransformRuleProvider provider){		
		TransformationInventory.getInstance().unbindRuleProvider(provider);
	}
}
