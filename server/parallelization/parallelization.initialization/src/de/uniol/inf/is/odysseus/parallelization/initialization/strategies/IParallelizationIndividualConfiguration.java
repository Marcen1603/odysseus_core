/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

/**
 * @author Dennis Nowak
 *
 */
public interface IParallelizationIndividualConfiguration {

	public void execute(Collection<IQueryBuildSetting<?>> settings);

	public ILogicalOperator getOperator();

	public void setBufferSize(int bufferSize);

	public void setParallelizationDegree(int degree);

}
