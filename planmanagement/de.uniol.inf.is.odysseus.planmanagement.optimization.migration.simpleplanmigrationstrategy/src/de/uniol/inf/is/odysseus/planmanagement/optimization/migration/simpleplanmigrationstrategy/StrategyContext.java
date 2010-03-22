package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

/**
 * 
 * @author Tobias Witt
 *
 */
class StrategyContext {

	private IOptimizer optimizer;
	private IEditableQuery runningQuery;
	private IPhysicalOperator newPlanRoot;
	private long ms;
	private List<BlockingBuffer<?>> blockingBuffers;
	private List<IPhysicalOperator> oldPlanOperatorsBeforeSources;
	
	private IPhysicalOperator union;
	private IPhysicalOperator select;
	private IPhysicalOperator lastOperatorOldPlan;
	private IPhysicalOperator lastOperatorNewPlan;

	public StrategyContext(IOptimizer optimizer, IEditableQuery runningQuery,
			IPhysicalOperator newPlanRoot) {
		this.optimizer = optimizer;
		this.runningQuery = runningQuery;
		this.newPlanRoot = newPlanRoot;
		this.blockingBuffers = new ArrayList<BlockingBuffer<?>>();
	}

	public IOptimizer getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(IOptimizer optimizer) {
		this.optimizer = optimizer;
	}

	public IEditableQuery getRunningQuery() {
		return runningQuery;
	}

	public void setRunningQuery(IEditableQuery runningQuery) {
		this.runningQuery = runningQuery;
	}

	public IPhysicalOperator getNewPlanRoot() {
		return newPlanRoot;
	}

	public void setNewPlanRoot(IPhysicalOperator newPlanRoot) {
		this.newPlanRoot = newPlanRoot;
	}

	public long getMs() {
		return ms;
	}

	public void setMs(long ms) {
		this.ms = ms;
	}

	public List<BlockingBuffer<?>> getBlockingBuffers() {
		return blockingBuffers;
	}

	public void setBlockingBuffers(List<BlockingBuffer<?>> blockingBuffers) {
		this.blockingBuffers = blockingBuffers;
	}

	public void setOldPlanOperatorsBeforeSources(
			List<IPhysicalOperator> oldPlanOperatorsBeforeSources) {
		this.oldPlanOperatorsBeforeSources = oldPlanOperatorsBeforeSources;
	}

	public List<IPhysicalOperator> getOldPlanOperatorsBeforeSources() {
		return oldPlanOperatorsBeforeSources;
	}

	public IPhysicalOperator getUnion() {
		return union;
	}

	public void setUnion(IPhysicalOperator union) {
		this.union = union;
	}

	public IPhysicalOperator getSelect() {
		return select;
	}

	public void setSelect(IPhysicalOperator select) {
		this.select = select;
	}

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

}
