/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

/**
 * @author Dennis Nowak
 *
 */
public abstract class AbstractParallelizationConfiguration implements IParallelizationIndividualConfiguration {

	protected int bufferSize;
	protected ILogicalOperator operator;
	protected int parallelizationDegree;

	protected AbstractParallelizationConfiguration(ILogicalOperator operator) {
		this.operator = operator;
	}

	@Override
	public ILogicalOperator getOperator() {
		return this.operator;
	}
	
	@Override
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	
	}

	@Override
	public void setParallelizationDegree(int parallelizationDegree) {
		this.parallelizationDegree = parallelizationDegree;

	}

}
