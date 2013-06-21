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

package de.uniol.inf.is.odysseus.plangenerator.generator.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PlanGenerationConfiguration;
import de.uniol.inf.is.odysseus.plangenerator.util.PartialPlanCollector;
import de.uniol.inf.is.odysseus.plangenerator.util.PartialPlanInserter;
import de.uniol.inf.is.odysseus.plangenerator.util.PlanGeneratorHelper;
import de.uniol.inf.is.odysseus.plangenerator.util.PredicateHelper;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

/**
 * @author Merlin Wasmann
 * 
 */
public abstract class AbstractPlanGenerationMethod implements
		IPlanGenerationMethod {

	private final static Logger LOG = LoggerFactory
			.getLogger(AbstractPlanGenerationMethod.class);

	protected int maxJoinCount = -1;
	// map an n (n-way join) to all created join plans for this n.
	protected List<Collection<ILogicalOperator>> nWayJoinList;

	private Map<ILogicalOperator, Set<ILogicalOperator>> partialJoinPlan2sources;

	private Set<ILogicalOperator> sources;
	private Set<JoinAO> joins;

	private PredicateHelper predicateHelper;

	private ILogicalOperator planCopy;

	private PartialPlanCollector partialPlanCollector;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.odysseus.plangenerator.generator.methods.IPlanGenerationMethod
	 * #
	 * generatePlans(de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator
	 * , de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.
	 * configuration.PlanGenerationConfiguration)
	 */
	@Override
	public abstract List<ILogicalOperator> generatePlans(ILogicalOperator plan,
			PlanGenerationConfiguration config, IOperatorOwner owner);

	protected void initialize(ILogicalOperator plan) {
		this.planCopy = PlanGeneratorHelper.copyPlan(plan);
		this.sources = PlanGeneratorHelper.getAccessOperators(this.planCopy);
		this.joins = PlanGeneratorHelper.getJoinOperators(this.planCopy);
		this.maxJoinCount = -1;
		this.maxJoinCount = getMaxJoinCount();
		this.predicateHelper = new PredicateHelper();
		this.predicateHelper.initialize(this.sources, this.joins);
		this.partialJoinPlan2sources = new HashMap<ILogicalOperator, Set<ILogicalOperator>>();
		this.nWayJoinList = new ArrayList<Collection<ILogicalOperator>>();
		this.partialPlanCollector = new PartialPlanCollector(this.planCopy);
	}

	/**
	 * Returns a collection of n-way join plans.
	 * 
	 * @param n
	 *            How many streams should be joined per plan? Minimum is 2.
	 * @param joins
	 * @return a collection of n-way join plans.
	 */
	protected Collection<ILogicalOperator> createJoinPlans(int n,
			List<Collection<ILogicalOperator>> existingJoinPlans) {

		if (n < 2 || existingJoinPlans == null) {
			// TODO: Exception werfen#
			LOG.error("n is too small, existingJoinPlans is null or plan is null.");
			return null;
		}

		if (n == 2) {
			// create 2-way join plans
			this.nWayJoinList.add(null);
			this.nWayJoinList.add(null);
			this.nWayJoinList.add(2, createTwoWayJoinPlans());
			return this.nWayJoinList.get(2);
		}

		Collection<ILogicalOperator> joinPlans = new HashSet<ILogicalOperator>();
		// Quellen sind immernoch paarweise verbunden
		for (ILogicalOperator joinPlan : existingJoinPlans.get(n - 1)) {

			Set<ILogicalOperator> missing = new HashSet<ILogicalOperator>();
			missing.addAll(this.sources);
			// cannot use removeAll, because these are only clones of the
			// original sources.
			for (ILogicalOperator access : this.partialJoinPlan2sources.get(joinPlan)) {
				missing.remove(PlanGeneratorHelper.getOriginal2Clone(access));
			}

			// Quellenpaare durchlaufen und prüfen welche Quelle noch fehlt.
			for (ILogicalOperator source : missing) {
				// create new joinClone for this source
				ILogicalOperator joinClone = PlanGeneratorHelper.copyPlan(joinPlan);
				
				ILogicalOperator sourceClone = source.clone();
				PlanGeneratorHelper.setOriginalForClone(sourceClone, source);
				// Join von joinClone mit fehlender Quelle.
				Pair<IPredicate<?>, Set<IRelationalPredicate>> predicatePair = this.predicateHelper
						.generatePredicate(
								this.partialJoinPlan2sources.get(joinPlan),
								sourceClone, joinPlan);
				JoinAO join = new JoinAO();
				if (predicatePair == null) {
					LOG.debug("Inserting true-predicate");
					predicatePair = new Pair<IPredicate<?>, Set<IRelationalPredicate>>(
							this.predicateHelper.createTruePredicate(),
							new HashSet<IRelationalPredicate>());
				}
				if (predicatePair.getE1() != null) {
					join.setPredicate(predicatePair.getE1());
				}
				// TODO: Ausgabeports müssen nicht 0 sein
				join.subscribeToSource(joinClone, 0, 0,
						joinClone.getOutputSchema());
				join.subscribeToSource(sourceClone, 1, 0,
						sourceClone.getOutputSchema());

				// set predicates as satisfied
				this.predicateHelper.setSatisfied(join, predicatePair.getE2());

				Set<ILogicalOperator> newSources = new HashSet<ILogicalOperator>();
				newSources.addAll(this.partialJoinPlan2sources.get(joinPlan));
				newSources.add(sourceClone);
				this.partialJoinPlan2sources.put(join, newSources);
				joinPlans.add(join);
			}
		}
		this.nWayJoinList.add(n, joinPlans);
		// this method should use the nWayJoinMap to reuse join plans.

		return this.nWayJoinList.get(n);
	}

	private Collection<ILogicalOperator> createTwoWayJoinPlans() {
		Collection<ILogicalOperator> twoWayJoins = new ArrayList<ILogicalOperator>();

		// Paare durchlaufen und aus jedem Paar einen neuen join-plan
		// erzeugen.
		for (Pair<ILogicalOperator, ILogicalOperator> pair : PlanGeneratorHelper
				.joinSets(this.sources)) {
			Pair<IPredicate<?>, Set<IRelationalPredicate>> predicatePair = this.predicateHelper
					.generatePredicate(pair);
			ILogicalOperator join = new JoinAO();
			if (predicatePair.getE1() != null) {
				join.setPredicate(predicatePair.getE1());
			}
			// connect both sources to the newly created join.
			join.subscribeToSource(pair.getE1(), 0, 0, pair.getE1()
					.getOutputSchema());
			join.subscribeToSource(pair.getE2(), 1, 0, pair.getE2()
					.getOutputSchema());

			// set the predicates as satisfied
			this.predicateHelper.setSatisfied(join, predicatePair.getE2());

			// add the joined sources to the set of used sources for this
			// partial plan.
			Set<ILogicalOperator> sourceSet = new HashSet<ILogicalOperator>();
			sourceSet.add(pair.getE1());
			sourceSet.add(pair.getE2());
			twoWayJoins.add(join);
			this.partialJoinPlan2sources.put(join, sourceSet);
		}
		return twoWayJoins;
	}

	/**
	 * Returns a collection of plans filled with the remaining operators to
	 * create semantically equivalent plans.
	 * 
	 * @param joinPlan
	 *            Plan to be filled.
	 * @param urPlan
	 *            Ur-Plan of this query.
	 * @return a collection of plans filled with the remaining operators. These
	 *         are copies of the given joinPlan
	 */
	protected ILogicalOperator fillPlanWithRemainingOperators(
			ILogicalOperator joinPlan) {
		Set<ILogicalOperator> subPlans = this.partialPlanCollector
				.collectPartialplans();
		PartialPlanInserter filler = new PartialPlanInserter(joinPlan, subPlans);
		return filler.fill();
	}

	/**
	 * Returns the n for the last n-way join. This n is used for the iteration
	 * through all join plans.
	 * 
	 * @param plan
	 * @return n for the last n-way join
	 */
	protected int getMaxJoinCount() {
		// for starters assume all sources are joined
		if (this.maxJoinCount > -1) {
			return this.maxJoinCount;
		}
		return this.sources.size();
	}

}
