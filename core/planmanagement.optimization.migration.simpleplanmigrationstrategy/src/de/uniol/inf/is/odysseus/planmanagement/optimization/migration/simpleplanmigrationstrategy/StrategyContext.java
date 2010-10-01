package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.IWindow;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

/**
 * 
 * Context of a plan migration.
 * 
 * @author Tobias Witt
 *
 */
class StrategyContext {

	private IOptimizer optimizer;
	private IQuery runningQuery;
	private IPhysicalOperator newPlanRoot;
	private IPhysicalOperator oldPlanRoot;
	private IWindow wMax;
	private List<BlockingBuffer<?>> blockingBuffers;
	private List<IPhysicalOperator> oldPlanOperatorsBeforeSources;
	
	private IPhysicalOperator union;
	private IPhysicalOperator select;
	private IPhysicalOperator lastOperatorOldPlan;
	private IPhysicalOperator lastOperatorNewPlan;

	public StrategyContext(IOptimizer optimizer, IQuery runningQuery,
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

	public IQuery getRunningQuery() {
		return runningQuery;
	}

	public void setRunningQuery(IQuery runningQuery) {
		this.runningQuery = runningQuery;
	}

	public IPhysicalOperator getNewPlanRoot() {
		return newPlanRoot;
	}

	public void setNewPlanRoot(IPhysicalOperator newPlanRoot) {
		this.newPlanRoot = newPlanRoot;
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

	public void setwMax(IWindow wMax) {
		this.wMax = wMax;
	}

	public IWindow getwMax() {
		return wMax;
	}

	public void setOldPlanRoot(IPhysicalOperator oldPlanRoot) {
		this.oldPlanRoot = oldPlanRoot;
	}

	public IPhysicalOperator getOldPlanRoot() {
		return oldPlanRoot;
	}

}
