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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.util.CollectSubPlanTopFirstjoinLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;

/**
 * Collects subplans between joins, between join and access and after the last
 * join in a plan.
 * 
 * @author Merlin Wasmann
 * 
 */
public class PartialPlanCollector {

	private ILogicalOperator plan;

	public PartialPlanCollector(ILogicalOperator plan) {
		this.plan = plan;
		PlanGeneratorHelper.setupInitialInputSchemata(plan);
	}

	public Set<ILogicalOperator> collectPartialplans() {
		Set<ILogicalOperator> subPlans = new HashSet<ILogicalOperator>();
		Pair<ILogicalOperator, ILogicalOperator> pair = getSubPlanRootToFirstJoin();
		subPlans.add(pair.getE2());

		ILogicalOperator first = pair.getE1();
		subPlans.addAll(collectPlans(first));
		return subPlans;
	}

	private Set<ILogicalOperator> collectPlans(ILogicalOperator op) {
		Set<ILogicalOperator> plans = new HashSet<ILogicalOperator>();
		if (op instanceof AccessAO) {
			// done
			return plans;
		}
		Pair<ILogicalOperator, ILogicalOperator> left = collectSubPlanFromBranch(
				(JoinAO) op, 0);
		Pair<ILogicalOperator, ILogicalOperator> right = collectSubPlanFromBranch(
				(JoinAO) op, 1);

		if (left.getE2() != null) {
			plans.add(left.getE2());
		}
		if (right.getE2() != null) {
			plans.add(right.getE2());
		}

		plans.addAll(collectPlans(left.getE1()));
		plans.addAll(collectPlans(right.getE1()));

		return plans;
	}

	/**
	 * collects a subplan starting from a join to a join or an access.
	 * 
	 * @param join
	 * @return a pair of ILogicalOperators. Left contains the next join or an
	 *         access. Right contains the collected subplan.
	 */
	private Pair<ILogicalOperator, ILogicalOperator> collectSubPlanFromBranch(
			JoinAO join, int joinInPort) {
		List<ILogicalOperator> operators = new ArrayList<ILogicalOperator>();
		LogicalSubscription[] inputs = new LogicalSubscription[2];
		// get input for the join at joinInPort
		ILogicalOperator next = join.getSubscribedToSource().toArray(inputs)[joinInPort]
				.getTarget();
		List<SDFSchema> inputSchemata = new ArrayList<SDFSchema>();
		while (!(next instanceof JoinAO) && !(next instanceof AccessAO)) {
			inputs = new LogicalSubscription[2];
			inputSchemata.add(next.getInputSchema(0));
			ILogicalOperator clone = PlanGeneratorHelper.cloneOperator(next);
			operators.add(clone);
			// get the next operator which was the input for next
			next = next.getSubscribedToSource().toArray(inputs)[0].getTarget();
		}
		for (int i = 0; i < operators.size() - 1; i++) {
			// TODO: die ports müssen nicht richtig sein...
			operators.get(i).subscribeToSource(operators.get(i + 1), 0, 0,
					operators.get(i + 1).getOutputSchema());
			PlanGeneratorHelper.setInputSchemaForOperator(operators.get(i), inputSchemata.get(i));
		}
		if (operators.isEmpty()) {
			return new Pair<ILogicalOperator, ILogicalOperator>(next, null);
		}
		return new Pair<ILogicalOperator, ILogicalOperator>(next,
				operators.get(0));
	}

	/**
	 * collects all operators after the last join, connects them and returns
	 * this subplan.
	 * 
	 * @param urPlan
	 * @return a pair if ILogicalOperators. Left contains the first join and
	 *         right contains the subplan.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Pair<ILogicalOperator, ILogicalOperator> getSubPlanRootToFirstJoin() {
		CollectSubPlanTopFirstjoinLogicalGraphVisitor<ILogicalOperator> visitor = new CollectSubPlanTopFirstjoinLogicalGraphVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(this.plan, visitor);
		Pair<ILogicalOperator, List<ILogicalOperator>> operators = visitor
				.getResult();
		List<ILogicalOperator> clones = new ArrayList<ILogicalOperator>();
		for (ILogicalOperator operator : operators.getE2()) {
			clones.add(PlanGeneratorHelper.cloneOperator(operator));
		}
		for (int i = 0; i < operators.getE2().size() - 1; i++) {
			// TODO: die ports müssen nicht richtig sein...
			clones.get(i).subscribeToSource(clones.get(i + 1), 0, 0,
					operators.getE2().get(i + 1).getOutputSchema());
		}
		return new Pair<ILogicalOperator, ILogicalOperator>(operators.getE1(),
				clones.get(clones.size() - 1));
	}

}
