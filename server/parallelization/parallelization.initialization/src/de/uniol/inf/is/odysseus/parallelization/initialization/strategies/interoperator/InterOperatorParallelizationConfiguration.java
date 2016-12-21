/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.exception.ParallelizationTransformationException;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AbstractParallelizationConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class InterOperatorParallelizationConfiguration extends AbstractParallelizationConfiguration {
	
	private static Logger LOG = LoggerFactory.getLogger(InterOperatorParallelizationConfiguration.class);

	private int degree;
	private int bufferSize;
	private String parallelizationStrategy;
	private String fragmentationStrategy;

	public InterOperatorParallelizationConfiguration(ILogicalOperator operator, String parallelizationStrategy,
			String fragmentationStrategy) {
		super(operator);
		this.parallelizationStrategy = parallelizationStrategy;
		this.fragmentationStrategy = fragmentationStrategy;
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
			InterOperatorIndividualInitializer.createInterIndividualConfiguration(settings,
					getOperator().getUniqueIdentifier(), degree, bufferSize, parallelizationStrategy,
					fragmentationStrategy);
		} catch (ParallelizationTransformationException e) {
			LOG.error("Creating individual parallelization configuration for operator " + operator + " failed.", e);
		}

	}

}
