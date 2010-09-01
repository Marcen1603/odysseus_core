package de.uniol.inf.is.odysseus.transform.engine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
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

	private static Logger logger = LoggerFactory.getLogger("transformation");

	public TransformationExecutor() {

	}

	@Override
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op, TransformationConfiguration config) throws TransformationException {		
		logger.info("Starting transformation of " + op + "...");		
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		logger.info("Before transformation: \n"+planPrinter.createString(op));
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
		TransformationEnvironment env = new TransformationEnvironment(config, concreteTransformInvent);

		addLogicalOperator(top, list, env);
		logger.trace("Processing rules...");
		// start transformation
		env.processEnvironment();
		logger.trace("Processing rules done.");

		IPhysicalOperator physicalPO = top.getPhysicalInput();
		if (physicalPO == null) {
			logger.warn("PhysicalInput of TopAO is null!");
			logger.warn("Current working memory:");
			logger.warn(env.getWorkingMemory().getCurrentContent().toString());
			logger.warn("Further information: \n" + env.getWorkingMemory().getDebugTrace());
		}

		IGraphNodeVisitor<IPhysicalOperator, ArrayList<IPhysicalOperator>> visitor = new FindQueryRootsVisitor<IPhysicalOperator>();
		AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, ?> walker = new AbstractGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, LogicalSubscription>();
		walker.prefixWalkPhysical(physicalPO, visitor);
		plan = visitor.getResult();
		if(plan.isEmpty()){
			logger.warn("Plan is empty! If transformation was successful, it is possible that there are no root-operators!");
		}
		SimplePlanPrinter<IPhysicalOperator> physicalPlanPrinter = new SimplePlanPrinter<IPhysicalOperator>();
		logger.info("After transformation: \n"+physicalPlanPrinter.createString(physicalPO));
		
		
		op.unsubscribeSink(top, 0, 0, op.getOutputSchema());
		logger.info("Transformation of " + op + " finished");
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

	public static Logger getLogger() {
		return logger;
	}

}
