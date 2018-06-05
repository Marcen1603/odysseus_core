/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AbstractParallelizationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelInterOperatorSetting;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class InterOperatorParallelizationConfiguration extends AbstractParallelizationConfiguration {
	
	private String parallelizationStrategy;
	private String fragmentationStrategy;

	public InterOperatorParallelizationConfiguration(ILogicalOperator operator, String parallelizationStrategy,
			String fragmentationStrategy) {
		super(operator);
		this.parallelizationStrategy = parallelizationStrategy;
		this.fragmentationStrategy = fragmentationStrategy;
	}

	@Override
	public void execute(Collection<IQueryBuildSetting<?>> settings) {
		String operatorId = this.getOperator().getUniqueIdentifier();
		ParallelOperatorConfiguration newConfiguration = new ParallelOperatorConfiguration();
		newConfiguration.setDegreeOfParallelization(parallelizationDegree);
		newConfiguration.setBufferSize(bufferSize);
		newConfiguration.setStartParallelizationId(operatorId);
		// TODO add possibility to build parallel region
		newConfiguration.setEndParallelizationId(null);
		newConfiguration.setMultithreadingStrategy(parallelizationStrategy);
		newConfiguration.setFragementationType(fragmentationStrategy);

		newConfiguration.setAssureSemanticCorrectness(true);
		newConfiguration.setUseParallelOperators(false);
		newConfiguration.setUseThreadedBuffer(false);

		ParallelInterOperatorSetting existing = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(ParallelInterOperatorSetting.class)) {
				existing = (ParallelInterOperatorSetting) setting;
			}
		}

		if (existing.getConfigurationForOperator(operatorId) != null) {
			existing.removeConfigurationForOperator(operatorId);
		}
		existing.addConfigurationForOperator(operatorId, newConfiguration);


	}

}
