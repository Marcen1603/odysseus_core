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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionFitness;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionMigrationFuzzyRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionPolicyRuleEngine;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption.IPlanAdaptionResourceMonitor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractPlanAdaptionEngine implements IPlanAdaptionEngine {
	
	private final static Logger LOG = LoggerFactory.getLogger(AbstractPlanAdaptionEngine.class);

	protected IPlanMigrationStrategy migrationStrategy;
	protected IPlanAdaptionResourceMonitor resourceMonitor;
	protected IPlanAdaptionFitness fitness;
	protected IPlanAdaptionMigrationFuzzyRuleEngine fuzzyRuleEngine;
	protected IPlanAdaptionPolicyRuleEngine policyRuleEngine;
	protected IServerExecutor executor;

	protected Set<IPhysicalQuery> stoppedQueries = new HashSet<IPhysicalQuery>();
	
	protected Set<IPhysicalQuery> currentlyAdaptedQueries = new HashSet<IPhysicalQuery>();
	
	@Override
	public abstract void adaptPlan(IPhysicalQuery query, ISession user);

	@Override
	public IPlanMigrationStrategy getPlanMigrationStrategy() {
		return this.migrationStrategy;
	}

	@Override
	public IPlanAdaptionResourceMonitor getAdaptionResourceMonitor() {
		return this.resourceMonitor;
	}

	@Override
	public IPlanAdaptionFitness getFitness() {
		return this.fitness;
	}

	@Override
	public IPlanAdaptionMigrationFuzzyRuleEngine getFuzzyRuleEngine() {
		return this.fuzzyRuleEngine;
	}

	@Override
	public IPlanAdaptionPolicyRuleEngine getPolicyRuleEngine() {
		return this.policyRuleEngine;
	}

	@Override
	public IServerExecutor getExecutor() {
		return this.executor;
	}

	@Override
	public abstract long getBlockedTime();

	@Override
	public abstract long getTimer();
	
	@Override
	public abstract boolean isRunning();

	@Override
	public abstract void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs);

	@Override
	public abstract void migrationFinished(IMigrationEventSource sender);

	@Override
	public abstract void migrationFailed(IMigrationEventSource sender, Throwable ex);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption
	 * .
	 * IPlanAdaptionEngine#setQueryAsStopped(de.uniol.inf.is.odysseus.core.server
	 * .planmanagement.query.IPhysicalQuery)
	 */
	@Override
	public void setQueryAsStopped(IPhysicalQuery query) {
		LOG.debug("Query: " +  query + " is set as stopped");
		this.stoppedQueries.add(query);
		if(this.currentlyAdaptedQueries.remove(query) && this.currentlyAdaptedQueries.isEmpty()) {
			getPolicyRuleEngine().start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption
	 * .
	 * IPlanAdaptionEngine#setQueryAsStarted(de.uniol.inf.is.odysseus.core.server
	 * .planmanagement.query.IPhysicalQuery)
	 */
	@Override
	public void setQueryAsStarted(IPhysicalQuery query) {
		LOG.debug("Query: " +  query + " is set as started");
		this.stoppedQueries.remove(query);
	}
	
	
}
