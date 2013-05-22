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

package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planadaption;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


/**
 * Interface for the PlanAdaption.
 * 
 * @author Merlin Wasmann
 *
 */
public interface IPlanAdaptionEngine extends IPlanModificationListener, IMigrationListener {

	public void adaptPlan(IPhysicalQuery query, ISession user);
	
	public IPlanMigrationStrategy getPlanMigrationStrategy();
	public IPlanAdaptionResourceMonitor getAdaptionResourceMonitor();
	public IPlanAdaptionFitness getFitness();
	public IPlanAdaptionMigrationRuleEngine getMigrationRuleEngine();
	public IPlanAdaptionPolicyRuleEngine getPolicyRuleEngine();
	public IServerExecutor getExecutor();
	
	public long getBlockedTime();
	public long getTimer();
	public boolean isRunning();
	
	/**
	 * Should be called when the query is stopped to avoid changing plans in the
	 * query while it is being stopped.
	 * 
	 * @param query
	 */
	public void setQueryAsStopped(IPhysicalQuery query);

	/**
	 * Should be called when a previously stopped query is started again, so it
	 * can be adapted.
	 * 
	 * @param query
	 */
	public void setQueryAsStarted(IPhysicalQuery query);
	
	/**
	 * Avoid cyclic dependency with executor
	 * 
	 * @param executor
	 */
	public void setExecutor(IExecutor executor);

	/**
	 * @param executor
	 */
	public void unsetExecutor(IExecutor executor);
}
