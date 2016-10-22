/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class IntraOperatorStrategyInitializer extends AbstractParallelizationStrategyInitializer {
	
	private static final Logger LOG = LoggerFactory.getLogger(IntraOperatorStrategyInitializer.class);

	// default to 0 avoids parallelizing all operators
	private static final int globalDegreeOfParallelization = 0;
	private static final int globalBuffersize = 0;

	@Override
	public void initialize(List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException {
		checkIfIntraOperatorSettingAlreadyExists(settings);

		ParallelIntraOperatorSettingValue value = new ParallelIntraOperatorSettingValue(
				globalDegreeOfParallelization, globalBuffersize);
		ParallelIntraOperatorSetting intraOperatorSetting = new ParallelIntraOperatorSetting(
				value);
		settings.add(intraOperatorSetting);
		LOG.debug("Added initial settings for automatic Intra_Operator parallelization");
	}

	private void checkIfIntraOperatorSettingAlreadyExists(List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException {
		for(IQueryBuildSetting<?> setting:settings) {
			if(setting.getClass().equals(ParallelIntraOperatorSetting.class)){
				throw new OdysseusScriptException("AUTOMATIC parallelization initializes other parallelization types. Do not use #PARALLELIZATION INTRA_OPERATOR.");
			}
		}
		
	}

	
}
