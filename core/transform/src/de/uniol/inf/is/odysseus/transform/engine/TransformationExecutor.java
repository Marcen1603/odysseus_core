/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.transform.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.FindQueryRootsVisitor;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

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
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator logicalOp,
			TransformationConfiguration config, ISession caller,
			IDataDictionary dd) throws TransformationException {
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO,
				"Starting transformation of " + logicalOp + "...");
		SimplePlanPrinter<ILogicalOperator> planPrinter = new SimplePlanPrinter<ILogicalOperator>();
		LoggerSystem.printlog(
				LOGGER_NAME,
				Accuracy.TRACE,
				"Before transformation: \n"
						+ planPrinter.createString(logicalOp));
		ArrayList<ILogicalOperator> list = new ArrayList<ILogicalOperator>();
		ArrayList<IPhysicalOperator> plan = new ArrayList<IPhysicalOperator>();
		TopAO top = null;
		if (logicalOp instanceof TopAO) {
			top = (TopAO) logicalOp;
		} else {
			top = new TopAO();
			logicalOp.subscribeSink(top, 0, 0, logicalOp.getOutputSchema());
		}
		/**
		 * creating a new transformation environment changes of inventory aren't
		 * considered! Otherwise a rule is able to work on different WM.
		 * therefore: cloning instance by copy constructor! that means:
		 * Singleton = global state concrete instance = local state for this
		 * instance
		 */
		TransformationInventory concreteTransformInvent = new TransformationInventory(
				TransformationInventory.getInstance());
		TransformationEnvironment env = new TransformationEnvironment(config,
				concreteTransformInvent, caller, dd);

		addLogicalOperator(top, list, env);
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE,
				"Processing rules...");
		// start transformation
		env.processEnvironment();
		LoggerSystem.printlog(LOGGER_NAME, Accuracy.TRACE,
				"Processing rules done.");

		Map<Integer, IPhysicalOperator> roots = top.getPhysInputOperators();
		if (roots == null || roots.size() == 0) {
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN,
					"PhysicalInput of TopAO is null!");
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN,
					"Current working memory:");
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN, env
					.getWorkingMemory().getCurrentContent().toString());
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.WARN,
					"Further information: \n"
							+ env.getWorkingMemory().getDebugTrace());
			throw new TransformationException("Error during transformation of "
					+ logicalOp);
		}

		// FIX: Now done by an explicit RenanePO
		// // if top operator has been rename, renaming is lost -->
		// setOutputSchema
		// // of physicalPO to Schema in subscription
		// if (logicalOp instanceof RenameAO) {
		// physicalPO.setOutputSchema(logicalOp.getOutputSchema());
		// }

		for (IPhysicalOperator physicalPO : roots.values()) {
			IGraphNodeVisitor<IPhysicalOperator, ArrayList<IPhysicalOperator>> visitor = new FindQueryRootsVisitor<IPhysicalOperator>();
			GenericGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, ?> walker = new GenericGraphWalker<ArrayList<IPhysicalOperator>, ILogicalOperator, LogicalSubscription>();
			walker.prefixWalkPhysical(physicalPO, visitor);
			plan = visitor.getResult();
			// Prefix Walker finds only roots that are not part of another query
			// physicalPO is in every case root of this query, so if not already
			// found, add to plan
			if (!plan.contains(physicalPO)) {
				plan.add(physicalPO);
			}
			if (plan.isEmpty()) {
				LoggerSystem
						.printlog(
								LOGGER_NAME,
								Accuracy.WARN,
								"Plan is empty! If transformation was successful, it is possible that there are no root-operators!");
			}
			SimplePlanPrinter<IPhysicalOperator> physicalPlanPrinter = new SimplePlanPrinter<IPhysicalOperator>();
			LoggerSystem.printlog(
					LOGGER_NAME,
					Accuracy.TRACE,
					"After transformation: \n"
							+ physicalPlanPrinter.createString(physicalPO));

			logicalOp.unsubscribeSink(top, 0, 0, logicalOp.getOutputSchema());
			LoggerSystem.printlog(LOGGER_NAME, Accuracy.INFO,
					"Transformation of " + logicalOp + " finished");
		}
		return plan;
	}

	private void addLogicalOperator(ILogicalOperator op,
			List<ILogicalOperator> inserted, TransformationEnvironment env) {
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

	public void addRuleProvider(ITransformRuleProvider provider) {
		TransformationInventory.getInstance().bindRuleProvider(provider);
	}

	public void removeRuleProvider(ITransformRuleProvider provider) {
		TransformationInventory.getInstance().unbindRuleProvider(provider);
	}
}
