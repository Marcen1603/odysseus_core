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
package de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * 
 * Context of a plan migration.
 * 
 * @author Tobias Witt, Merlin Wasmann
 *
 */
class StrategyContext {

	private IServerExecutor executor;
	private IPhysicalQuery runningQuery;
	private IPhysicalOperator newPlanRoot;
	private IPhysicalOperator oldPlanRoot;
//	private IWindow wMax;
	private List<BufferPO<?>> buffersPOs;
	private List<IPhysicalOperator> oldPlanOperatorsBeforeSources;
	
	private IPhysicalOperator router;
	//private IPhysicalOperator select;
	private IPhysicalOperator lastOperatorOldPlan;
	private IPhysicalOperator lastOperatorNewPlan;
	
	private long migrationStart;

	public StrategyContext(IServerExecutor executor, IPhysicalQuery runningQuery,
			IPhysicalOperator newPlanRoot) {
		this.executor = executor;
		this.runningQuery = runningQuery;
		this.newPlanRoot = newPlanRoot;
		this.buffersPOs = new ArrayList<BufferPO<?>>();
	}

	public IServerExecutor getExecutor() {
		return this.executor;
	}

	public IPhysicalQuery getRunningQuery() {
		return runningQuery;
	}

	public void setRunningQuery(IPhysicalQuery runningQuery) {
		this.runningQuery = runningQuery;
	}

	public IPhysicalOperator getNewPlanRoot() {
		return newPlanRoot;
	}

	public void setNewPlanRoot(IPhysicalOperator newPlanRoot) {
		this.newPlanRoot = newPlanRoot;
	}
	
	public void addBufferPO(BufferPO<?> buffer) {
		this.buffersPOs.add(buffer);
	}

	public List<BufferPO<?>> getBufferPOs() {
		return buffersPOs;
	}

	public void setBufferPOs(List<BufferPO<?>> blockingBuffers) {
		this.buffersPOs = blockingBuffers;
	}

	public void setOldPlanOperatorsBeforeSources(
			List<IPhysicalOperator> oldPlanOperatorsBeforeSources) {
		this.oldPlanOperatorsBeforeSources = oldPlanOperatorsBeforeSources;
	}

	public List<IPhysicalOperator> getOldPlanOperatorsBeforeSources() {
		return oldPlanOperatorsBeforeSources;
	}

	public IPhysicalOperator getRouter() {
		return router;
	}

	public void setRouter(IPhysicalOperator router) {
		this.router = router;
	}

//	public IPhysicalOperator getSelect() {
//		return select;
//	}
//
//	public void setSelect(IPhysicalOperator select) {
//		this.select = select;
//	}

	public IPhysicalOperator getLastOperatorOldPlan() {
		return lastOperatorOldPlan;
	}

	public void setLastOperatorOldPlan(IPhysicalOperator lastOperatorOldPlan) {
		this.lastOperatorOldPlan = lastOperatorOldPlan;
	}

	public IPhysicalOperator getLastOperatorNewPlan() {
		return lastOperatorNewPlan;
	}

	public void setLastOperatorNewPlan(IPhysicalOperator lastOperatorNewPlan) {
		this.lastOperatorNewPlan = lastOperatorNewPlan;
	}

//	public void setwMax(IWindow wMax) {
//		this.wMax = wMax;
//	}
//
//	public IWindow getwMax() {
//		return wMax;
//	}

	public void setOldPlanRoot(IPhysicalOperator oldPlanRoot) {
		this.oldPlanRoot = oldPlanRoot;
	}

	public IPhysicalOperator getOldPlanRoot() {
		return oldPlanRoot;
	}
	
	public void setMigrationStart(long start) {
		this.migrationStart = start;
	}

	public long getMigrationStart() {
		return this.migrationStart;
	}
}
