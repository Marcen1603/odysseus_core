/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.exception.ParallelizationTransormationException;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelInterOperatorSetting;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;

/**
 * @author Dennis Nowak
 *
 */
public class InterOperatorIndividualInitializer {
	
	public static void createInterIndividualConfiguration(Collection<IQueryBuildSetting<?>> settings, String operatorId,
			int degree, int bufferSize, String parallelizationStrategy, String fragmentationStrategy)
			throws ParallelizationTransormationException {
		ParallelOperatorConfiguration newConfiguration = new ParallelOperatorConfiguration();
		newConfiguration.setDegreeOfParallelization(degree);
		newConfiguration.setBufferSize(bufferSize);
		newConfiguration.setStartParallelizationId(operatorId);
		//TODO add possibility to build parallel region
		newConfiguration.setEndParallelizationId(null);
		newConfiguration.setMultithreadingStrategy(parallelizationStrategy);
		newConfiguration.setFragementationType(fragmentationStrategy);
		
		newConfiguration.setAssureSemanticCorrectness(true);
		newConfiguration.setUseParallelOperators(false);
		newConfiguration.setUseThreadedBuffer(true);
		
		ParallelInterOperatorSetting existing =null;
		for(IQueryBuildSetting<?> setting:settings) {
			if(setting.getClass().equals(ParallelInterOperatorSetting.class)) {
				existing = (ParallelInterOperatorSetting) setting;
			}
		}
		
		if(existing.getConfigurationForOperator(operatorId) == null) {
		existing.addConfigurationForOperator(operatorId, newConfiguration);
		} else {
			throw new ParallelizationTransormationException("No Configuration existing.");
		}
		
	}

}
