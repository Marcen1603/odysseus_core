/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.planmigration.noplanmigrationstrategie;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IPlanMigratable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.planmigration.IPlanMigrationStrategy;

//import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
//import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
//import de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable;
//import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;
//import de.uniol.inf.is.odysseus.planmanagement.plan.IExecutionPlan;
//import de.uniol.inf.is.odysseus.planmanagement.query.IPhysicalQuery;

/**
 * This is the standard plan migration strategy. No migration is performed. The
 * new execution plan will be used unmodified.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoPlanMigrationStrategy implements IPlanMigrationStrategy {
	
	private Set<IMigrationListener> listener;
	
	public NoPlanMigrationStrategy() {
		this.listener = new HashSet<IMigrationListener>();
	}
	
	@Override
	public String getName() {
		return "No Migration Strategy";
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategie#migratePlan(de.uniol.inf.is.odysseus.planmanagement.optimization.IPlanMigratable, de.uniol.inf.is.odysseus.physicaloperator.plan.IExecutionPlan)
	 */
	@Override
	public IExecutionPlan migratePlan(IPlanMigratable sender,
			IExecutionPlan newExecutionPlan) {
		return newExecutionPlan;
	}

	@Override
	public void migrateQuery(IServerExecutor sender,
			IPhysicalQuery runningQuery, List<IPhysicalOperator> newPlanRoots) {
	}

	@Override
	public void addMigrationListener(IMigrationListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void removeMigrationListener(IMigrationListener listener) {
		if(this.listener.contains(listener)) {
			this.listener.remove(listener);
		}
	}

	@Override
	public void fireMigrationFinishedEvent(IMigrationEventSource sender) {
		for(IMigrationListener listener : this.listener) {
			listener.migrationFinished(sender);
		}
	}

	@Override
	public void migrationFinished(IMigrationEventSource sender) {
		// TODO: nop
	}

	@Override
	public IPhysicalQuery getPhysicalQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasPhysicalQuery() {
		return false;
	}

	@Override
	public void fireMigrationFailedEvent(IMigrationEventSource sender,
			Throwable ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void migrationFailed(IMigrationEventSource sender, Throwable ex) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmigration.IMigrationStrategy#isMigratable(de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery)
	 */
	@Override
	public boolean isMigratable(IPhysicalQuery query) {
		return true;
	}
}
