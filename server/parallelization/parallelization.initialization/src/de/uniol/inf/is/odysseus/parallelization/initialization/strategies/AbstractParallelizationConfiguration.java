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

	protected ILogicalOperator operator;

	protected AbstractParallelizationConfiguration(ILogicalOperator operator) {
		this.operator = operator;
	}

	@Override
	public ILogicalOperator getOperator() {
		return this.operator;
	}

}
