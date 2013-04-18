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

package de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.adaptionengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICost;
import de.uniol.inf.is.odysseus.core.server.costmodel.ICostModel;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.exception.QueryOptimizationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionFitness;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionMigrationFuzzyRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionPolicyListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionPolicyRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionResourceMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.costmodel.PlanMigration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.exception.MigrationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.planmanagement.optimization.planadaption.standardpolicyruleengine.PlanAdaptionPolicyRuleEngine;

/**
 * 
 * @author Merlin Wasmann
 * 
 */
public class PlanAdaptionEngine extends AbstractPlanAdaptionEngine implements
		IPlanAdaptionPolicyListener {

	private final static Logger LOG = LoggerFactory
			.getLogger(PlanAdaptionEngine.class);

	private Map<String, ICostModel<?>> migrationCostModels = new HashMap<String, ICostModel<?>>();
	private ICostModel<PlanMigration> selectedCostModel = null;

	private Map<IPhysicalQuery, List<IPhysicalOperator>> queryToOriginalPlan = new HashMap<IPhysicalQuery, List<IPhysicalOperator>>();

	private long blockedTime = OdysseusConfiguration.getLong(
			"adaption_blockingTime", 60000);
	private long timerValue = OdysseusConfiguration.getLong("adaption_timer",
			10000);

	private int runningQueries = 0;
	
	private boolean useFirst = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void adaptPlan(IPhysicalQuery physicalQuery, ISession user) {
		if(this.stoppedQueries.contains(physicalQuery)) {
			LOG.debug("Query: " + physicalQuery + " is not adapted because it is stopped.");
			this.currentlyAdaptedQueries.remove(physicalQuery);
			return;
		}
		if(this.useFirst) {
			LOG.debug("Only one migration allowed");
			for(int i : ((IServerExecutor) getExecutor()).getLogicalQueryIds()) {
				getExecutor().stopQuery(i, user);
			}
			return;
		}
		LOG.debug("Adapt plan with timer: " + this.timerValue
				+ " and blocked: " + this.blockedTime);
		List<IPhysicalOperator> originalRoots = new ArrayList<IPhysicalOperator>();
		originalRoots.addAll(physicalQuery.getRoots());
		this.queryToOriginalPlan.put(physicalQuery, originalRoots);
		ILogicalQuery query = physicalQuery.getLogicalQuery();
		Pair<ILogicalOperator, ICost<ILogicalOperator>> fittest = this.fitness
				.pickFittestPlan(query);
		if (fittest.getE2() == null) {
			// nothing to be done here.
			LOG.debug("Already using fittest plan");
			// return;
			// FIXME (Merlin): nur zu testzwecken. wir wollen migrationen sehen!
			fittest.setE1(query.getAlternativeLogicalPlans().get((useFirst ? 1 : 0)));
		}
		// gehen wir davon aus, dass es erstmal nur eine wurzel gibt.
		IPhysicalOperator oldPlan = physicalQuery.getPhysicalChilds().get(0);

		// set the new plan in the query to translate it.
		query.setLogicalPlan(fittest.getE1(), false);

		List<ILogicalQuery> logicalQueries = new ArrayList<ILogicalQuery>();
		logicalQueries.add(query);

		// translation of the fitter logical plan
		IPhysicalQuery newPhysicalQuery = this.executor.getCompiler()
				.transform(
						query,
						this.executor.getBuildConfigForQuery(query)
								.getTransformationConfiguration(), user,
						this.executor.getDataDictionary());
		List<IPhysicalOperator> newPlanRoots = newPhysicalQuery.getRoots();

		// remove the newPhysicalQuery as Owner from the newly transformed physical plan
		RemoveOwnersGraphVisitor<IOwnedOperator> removeVisitor = new RemoveOwnersGraphVisitor<>(
				newPhysicalQuery);
		GenericGraphWalker walker = new GenericGraphWalker();
		for (IPhysicalOperator root : newPlanRoots) {
			walker.prefixWalkPhysical(root, removeVisitor);
		}

		// create planmigration
		// TODO: was passiert mit mehreren Roots?
		PlanMigration migration = new PlanMigration(oldPlan,
				newPlanRoots.get(0), this.migrationStrategy);
		// estimate migration costs
		ICost<PlanMigration> migrationCost = this.resourceMonitor
				.getPlanMigrationCost(migration);

		if (this.fuzzyRuleEngine.evaluate(fittest.getE2(), migrationCost)) {
			// migrate
			try {
				this.migrationStrategy.migrateQuery(this.executor,
						physicalQuery, newPlanRoots);
				this.useFirst = !this.useFirst;
			} catch (MigrationException ex) {
				throw new QueryOptimizationException(ex);
			}
		}
	}

	public void bindExecutor(IExecutor executor) {
		this.executor = (IServerExecutor) executor;
		this.executor.addPlanModificationListener(this);
		LOG.debug("Executor bound");
	}

	public void unbindExecutor(IExecutor executor) {
		if (executor.equals(this.executor)) {
			this.executor.removePlanModificationListener(this);
			this.executor = null;
			LOG.debug("Executor unbound");
		}
	}

	public void bindPlanMigrationStrategy(IPlanMigrationStrategy strategy) {
		this.migrationStrategy = strategy;
		this.migrationStrategy.addMigrationListener(this);
		LOG.debug("PlanMigrationStrategy: "
				+ strategy.getClass().getSimpleName() + " bound");
	}

	public void unbindPlanMigrationStrategy(IPlanMigrationStrategy strategy) {
		if (strategy.equals(this.migrationStrategy)) {
			this.migrationStrategy.removeMigrationListener(this);
			this.migrationStrategy = null;
			LOG.debug("PlanMigrationStrategy: "
					+ strategy.getClass().getSimpleName() + " unbound");
		}
	}

	public void bindAdaptionResourceMonitor(IPlanAdaptionResourceMonitor monitor) {
		this.resourceMonitor = monitor;
		LOG.debug("PlanAdaptionResourceMonitor bound");
	}

	public void unbindAdaptionResourceMonitor(
			IPlanAdaptionResourceMonitor monitor) {
		if (monitor.equals(this.resourceMonitor)) {
			this.resourceMonitor = null;
			LOG.debug("PlanAdaptionResourceMonitor unbound");
		}
	}

	public void bindAdaptionFitness(IPlanAdaptionFitness fitness) {
		this.fitness = fitness;
		LOG.debug("PlanAdaptionFitness bound");
	}

	public void unbindAdaptionFitness(IPlanAdaptionFitness fitness) {
		if (fitness.equals(this.fitness)) {
			this.fitness = null;
			LOG.debug("PlanAdaptionFitness unbound");
		}
	}

	public void bindFuzzyRuleEngine(IPlanAdaptionMigrationFuzzyRuleEngine rule) {
		this.fuzzyRuleEngine = rule;
		LOG.debug("PlanAdaptionMigrationFuzzyRuleEngine bound");
	}

	public void unbindFuzzyRuleEngine(IPlanAdaptionMigrationFuzzyRuleEngine rule) {
		if (rule.equals(this.fuzzyRuleEngine)) {
			this.fuzzyRuleEngine = null;
			LOG.debug("PlanAdaptionMigrationFuzzyRuleEngine unbound");
		}
	}

	public void bindPolicyRuleEngine(IPlanAdaptionPolicyRuleEngine rule) {
		this.policyRuleEngine = rule;
		LOG.debug("PlanAdaptionPolicyRuleEngine bound");
		this.policyRuleEngine.register(this);
		((PlanAdaptionPolicyRuleEngine) this.policyRuleEngine).initialize(
				getBlockedTime(), getTimer());
		LOG.debug("Policy Enginge initialized");
	}

	public void unbindPolicyRuleEngine(IPlanAdaptionPolicyRuleEngine rule) {
		if (rule.equals(this.policyRuleEngine)) {
			this.policyRuleEngine.unregister(this);
			this.policyRuleEngine = null;
			LOG.debug("PlanAdaptionPolicyRuleEngine unbound");
		}
	}

	public void bindCostModel(ICostModel<?> model) {
		this.migrationCostModels.put(model.getClass().getSimpleName(), model);
		LOG.debug("CostModel: " + model.getClass().getSimpleName() + " bound");
		if (getSelectedCostModel() == null) {
			selectCostModel(model.getClass().getSimpleName());
		}
	}

	public void unbindCostModel(ICostModel<?> model) {
		if (this.migrationCostModels.containsKey(model.getClass()
				.getSimpleName())) {
			this.migrationCostModels.remove(model.getClass().getSimpleName());
			LOG.debug("CostModel: " + model.getClass().getSimpleName()
					+ " unbound");
		}
	}

	@SuppressWarnings("unchecked")
	public void selectCostModel(String name) {
		if (!this.migrationCostModels.containsKey(name)) {
			throw new RuntimeException("CostModel " + name + " not found.");
		}
		this.selectedCostModel = (ICostModel<PlanMigration>) this.migrationCostModels
				.get(name);
	}

	String getSelectedCostModel() {
		if (this.selectedCostModel == null) {
			return null;
		}
		return this.selectedCostModel.getClass().getSimpleName();
	}

	ICostModel<PlanMigration> getSelectedCostModelInstance() {
		return this.selectedCostModel;
	}

	@Override
	public void adaptionEventFired(IPlanAdaptionPolicyRuleEngine sender) {
		Collection<IPhysicalQuery> physicalQueries = ((IServerExecutor) this.executor)
				.getExecutionPlan().getQueries();
		this.policyRuleEngine.stop();
		for (IPhysicalQuery query : physicalQueries) {
			boolean noAdapt = (boolean) query.getParameter("noAdaption");
			if (query.isOpened() && !noAdapt) {
				LOG.debug("Adapting PhysicalQuery: " + query);
				// only adapt running queries.
				this.currentlyAdaptedQueries.add(query);
				adaptPlan(query, query.getSession());
			} else {
				LOG.debug("PhysicalQuery: " + query + " has not been adapted");
			}
		}
		if (this.currentlyAdaptedQueries.isEmpty()) {
			this.policyRuleEngine.start(false);
		}
	}

	@Override
	public long getBlockedTime() {
		return this.blockedTime;
	}

	@Override
	public long getTimer() {
		return this.timerValue;
	}

	@Override
	public boolean isRunning() {
		return ((PlanAdaptionPolicyRuleEngine) this.policyRuleEngine)
				.getTimer().isRunning();
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		LOG.debug("Plan modification event: " + eventArgs);
		if (eventArgs.getEventType() == PlanModificationEventType.QUERY_START) {
			if (this.runningQueries == 0) {
				this.policyRuleEngine.start(true);
			}
			this.runningQueries++;
		} else if (eventArgs.getEventType() == PlanModificationEventType.QUERY_STOP) {
			this.runningQueries--;
			if (this.runningQueries == 0) {
				this.policyRuleEngine.stop();
			}
		}
	}

	@Override
	public void migrationFinished(IMigrationEventSource sender) {
		// migration is finished so the adaption process can be started again.
		this.currentlyAdaptedQueries.remove(sender.getPhysicalQuery());

		if (this.currentlyAdaptedQueries.isEmpty()) {
			this.policyRuleEngine.start(true);
		} else {
			LOG.debug("There are still queries left that are currently adapting");
		}
	}

	@Override
	public void migrationFailed(IMigrationEventSource sender, Throwable ex) {
		// revert all possible changes
		IPhysicalQuery query = sender.getPhysicalQuery();
		List<IPhysicalOperator> originalRoots = this.queryToOriginalPlan
				.get(query);
		query.initializePhysicalRoots(originalRoots);
		LOG.error("Migration failed!", ex);
		this.currentlyAdaptedQueries.remove(sender.getPhysicalQuery());

		if (this.currentlyAdaptedQueries.isEmpty()) {
			this.policyRuleEngine.start(true);
		} else {
			LOG.debug("There are still queries left that are currently adapting");
		}
	}

}
