/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;

/**
 * 
 * @author Merlin Wasmann
 * 
 */
public class PartialPlanInserter {

	private static final Logger LOG = LoggerFactory
			.getLogger(PartialPlanInserter.class);

	private ILogicalOperator joinPlan;
	private Set<ILogicalOperator> partialPlans;

	private ILogicalOperator copy;
	private Set<? extends ILogicalOperator> joins;

	public PartialPlanInserter(ILogicalOperator joinPlan,
			Set<ILogicalOperator> subPlans) {
		this.joinPlan = joinPlan;
		this.partialPlans = subPlans;
		this.copy = PlanGeneratorHelper.copyPlan(this.joinPlan);
		this.joins = PlanGeneratorHelper.getJoinOperators(this.copy);
	}

	/**
	 * Fills the joinPlan with the subPlans.
	 * 
	 * @return a copy of the joinPlan filled with the subPlans.
	 */
	public ILogicalOperator fill() {
		for (ILogicalOperator op : this.joins) {
			if (op.getSubscriptions().isEmpty()) {
				Set<ILogicalOperator> removeSubplans = new HashSet<ILogicalOperator>();
				// root
				for (ILogicalOperator subPlan : this.partialPlans) {
					// op is the root operator
					if (isInsertableAsNewRoot(op, subPlan)) {
						// set the new root as root for the plan
						this.copy = insertPartialPlanAsNewRoot(subPlan, op, 0,
								0);
						removeSubplans.add(subPlan);
					}
				}
				this.partialPlans.removeAll(removeSubplans);
			}
			ILogicalOperator left = getMinimalCandidate(op, 0);
			ILogicalOperator right = getMinimalCandidate(op, 1);

			if (left != null && right != null) {
				if (left.equals(right)) {
					LOG.error("Same partial plan for both ports found.");
				}
			}

			if (left != null) {
				insertPartialPlan(left, op, 0, 0, 0);
				this.partialPlans.remove(left);
			}
			if (right != null) {
				insertPartialPlan(right, op, 1, 0, 0);
				this.partialPlans.remove(right);
			}
		}
		if (!this.partialPlans.isEmpty()) {
			LOG.error("Not all subplans were used. There are "
					+ this.partialPlans.size() + " which were not used.");
			for (ILogicalOperator unusedPlan : this.partialPlans) {
				PlanGeneratorHelper.printErrorPlan("[UnusedPlan]", unusedPlan);
			}
		}
		validateWindowPositions();

		PlanGeneratorHelper.printPlan("Finished Plan:",
				this.copy);
		
		return this.copy;
	}

	/**
	 * Gets the minimal candidate for one join and a port.
	 * 
	 * @param join
	 * @param port
	 * @return
	 */
	private ILogicalOperator getMinimalCandidate(ILogicalOperator join, int port) {
		Set<ILogicalOperator> candidates = getCandidates(join, port);
		if (candidates.isEmpty()) {
			return null;
		}
		if (candidates.size() == 1) {
			return candidates.iterator().next();
		}
		SDFSchema sourceSchema = join.getSubscribedToSource(port).getTarget()
				.getOutputSchema();
		ILogicalOperator minimalCandidate = null;
		int schemaDifference = 0;
		for (ILogicalOperator candidate : candidates) {
			SDFSchema candidateInputSchema = PlanGeneratorHelper
					.getInputSchemaForOperator(getLeaf(candidate));
			SDFSchema intersect = SDFSchema.intersection(sourceSchema,
					candidateInputSchema);
			int difference = sourceSchema.getAttributes().size()
					- intersect.getAttributes().size();
			if (minimalCandidate == null || difference < schemaDifference) {
				minimalCandidate = candidate;
				schemaDifference = difference;
			}
		}
		return minimalCandidate;
	}

	private Set<ILogicalOperator> getCandidates(ILogicalOperator join, int port) {
		Set<ILogicalOperator> candidates = new HashSet<ILogicalOperator>();
		for (ILogicalOperator partialPlan : this.partialPlans) {
			if (isInsertable(partialPlan, join, port)) {
				candidates.add(partialPlan);
			}
		}
		return candidates;
	}

	private void validateWindowPositions() {
		Set<ILogicalOperator> sources = PlanGeneratorHelper
				.getAccessOperators(this.copy);
		Set<ILogicalOperator> unWindowedSources = new HashSet<ILogicalOperator>();
		for (ILogicalOperator source : sources) {
			if (!PlanGeneratorHelper.hasWindowBeforeJoin(source)) {
				unWindowedSources.add(source);
			}
		}
		Set<WindowAO> windows = PlanGeneratorHelper
				.getWindowOperators(this.copy);
		Set<Pair<ILogicalOperator, WindowAO>> repaired = new HashSet<Pair<ILogicalOperator, WindowAO>>();
		// TODO: Prüfen welches Window zu welchem Access/Stream gehört
		for (ILogicalOperator unWindowed : unWindowedSources) {
			SDFSchema sourceOutput = unWindowed.getOutputSchema();
			for (WindowAO window : windows) {
				SDFSchema windowInput = PlanGeneratorHelper
						.getInputSchemaForOperator(window);
				SDFSchema intersect = SDFSchema.intersection(sourceOutput,
						windowInput);
				if (intersect.getAttributes().size() == sourceOutput
						.getAttributes().size()) {
					// the window is associate with this source.
					repaired.add(new Pair<ILogicalOperator, WindowAO>(unWindowed,
							window));
				}
			}
		}
		for (Pair<ILogicalOperator, WindowAO> pair : repaired) {
			ILogicalOperator after = pair.getE1().getSubscriptions().iterator()
					.next().getTarget();
			int sinkInPort = 0;
			for(LogicalSubscription sub : after.getSubscribedToSource()) {
				if(sub.getTarget().equals(pair.getE1())) {
					sinkInPort = sub.getSinkInPort();
				}
			}
			RestructHelper.removeOperator(pair.getE2(), false);
			RestructHelper.insertOperator(pair.getE2(), after, sinkInPort, 0, 0);
		}
	}

	// private Set<ILogicalOperator> insertInsertablePartialPlans(
	// ILogicalOperator op) {
	// op.updateSchemaInfos();
	// Set<ILogicalOperator> removeSubplans = new HashSet<ILogicalOperator>();
	// for (ILogicalOperator subPlan : this.partialPlans) {
	// if (op.getSubscriptions().isEmpty()) {
	// // op is the root operator
	// if (isInsertableAsNewRoot(op, subPlan)) {
	// // set the new root as root for the plan
	// this.copy = insertPartialPlanAsNewRoot(subPlan, op, 0, 0);
	// removeSubplans.add(subPlan);
	// }
	// continue;
	// }
	// if (op.getNumberOfInputs() > 1 && isInsertable(subPlan, op, 1)
	// && removeSubplans.contains(subPlan)) {
	// insertPartialPlan(subPlan, op, 1, 0, 0);
	// removeSubplans.add(subPlan);
	// continue;
	// }
	// if (isInsertable(subPlan, op, 0)
	// && !removeSubplans.contains(subPlan)) {
	// insertPartialPlan(subPlan, op, 0, 0, 0);
	// removeSubplans.add(subPlan);
	// continue;
	// }
	// }
	// return removeSubplans;
	// }

	private boolean isInsertable(ILogicalOperator subPlan,
			ILogicalOperator futureFather, int fatherInPort) {
		if (subPlan == null || futureFather == null
				|| futureFather.getSubscribedToSource(fatherInPort) == null) {
			return false;
		}

		ILogicalOperator futureChild = futureFather.getSubscribedToSource(
				fatherInPort).getTarget();
		// get the input schema of the last leaf in the partial plan.
		ILogicalOperator leaf = getLeaf(subPlan);
		SDFSchema subSchema = PlanGeneratorHelper
				.getInputSchemaForOperator(leaf);
		// get the output schema of the future child.
		SDFSchema childSchema = futureChild.getOutputSchema();
		// check if the schemata are null and if the future child's output
		// schema could satisfy the input schema of the last leaf in the partial
		// plan.
		if (subSchema != null
				&& childSchema != null
				&& childSchema.getAttributes().containsAll(
						subSchema.getAttributes())) {
			return true;
		}
		return false;
	}

	private boolean isInsertableAsNewRoot(ILogicalOperator root,
			ILogicalOperator futureRoot) {
		List<SDFAttribute> rootOutput = root.getOutputSchema().getAttributes();
		ILogicalOperator leaf = getLeaf(futureRoot);
		SDFSchema leafInputSchema = PlanGeneratorHelper
				.getInputSchemaForOperator(leaf);
		List<SDFAttribute> futureRootInput = leafInputSchema.getAttributes();
		return rootOutput.containsAll(futureRootInput)
				&& futureRootInput.containsAll(rootOutput);
	}

	/**
	 * Almost the same as insertOperator in RestructHelper but for partial plans
	 * and not only for an operator
	 * 
	 * @param partialPlan
	 *            Partial plan to be inserted as a child for the
	 *            futureFather-operator.
	 * @param futureFather
	 *            The Operator which will be the father of the inserted partial
	 *            plan.
	 * @param futureFatherInPort
	 *            The port on which the partial plan will put his data into the
	 *            futureFather.
	 * @param partialPlanInPort
	 *            The input port for the partial plan.
	 * @param partialPlanOutPort
	 *            The output port of the partial plan.
	 * @return
	 */
	private boolean insertPartialPlan(ILogicalOperator partialPlan,
			ILogicalOperator futureFather, int futureFatherInPort,
			int partialPlanInPort, int partialPlanOutPort) {
		LogicalSubscription source = futureFather
				.getSubscribedToSource(futureFatherInPort);
		futureFather.unsubscribeFromSource(source);
		ILogicalOperator leaf = getLeaf(partialPlan);
		source.getTarget()
				.subscribeSink(leaf, partialPlanInPort,
						source.getSourceOutPort(),
						source.getTarget().getOutputSchema());
		partialPlan.subscribeSink(futureFather, futureFatherInPort,
				partialPlanOutPort, partialPlan.getOutputSchema());
		return true;
	}

	private ILogicalOperator insertPartialPlanAsNewRoot(
			ILogicalOperator partialPlan, ILogicalOperator root,
			int rootOutPort, int partialPlanInPort) {
		root.subscribeSink(partialPlan, partialPlanInPort, rootOutPort,
				root.getOutputSchema());
		return partialPlan;
	}

	private ILogicalOperator getLeaf(ILogicalOperator plan) {
		LogicalSubscription next = plan.getSubscribedToSource(0);
		while (next != null) {
			if (next.getTarget().getSubscribedToSource(0) == null) {
				return next.getTarget();
			} else {
				next = next.getTarget().getSubscribedToSource(0);
			}
		}
		return plan;
	}

}
