/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.exception.ParallelizationTransormationException;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AbstractParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class InterOperatorParallelizationConfiguration extends AbstractParallelizationConfiguration {


	private int degree;
	private int bufferSize;
	private String parallelizationStrategy;
	private String fragmentationStrategy;


	public InterOperatorParallelizationConfiguration(ILogicalOperator operator, String parallelizationStrategy, String fragmentationStrategy) {
		super(operator);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void setParallelizationDegree(int degree) {
		this.degree = degree;
		
	}
	
	@Override
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		
	}


	@Override
	public void execute(Collection<IQueryBuildSetting<?>> settings) {
		try {
			InterOperatorIndividualInitializer.createInterIndividualConfiguration(settings, getOperator().getUniqueIdentifier(), degree, bufferSize, parallelizationStrategy, fragmentationStrategy);
		} catch (ParallelizationTransormationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
