/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.planrewriter.AutomaticParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class AutomaticStrategyInitializer extends AbstractParallelizationStrategyInitializer {

	@Override
	public void initialize(List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException {
		PreTransformationHandlerParameter exists = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(PreTransformationHandlerParameter.class)) {
				PreTransformationHandlerParameter preTransformationHandlerParameter = (PreTransformationHandlerParameter) setting;
				exists = preTransformationHandlerParameter;
				for (HandlerParameterPair preTransformationParameterPair : preTransformationHandlerParameter
						.getPairs()) {
					if (preTransformationParameterPair.name
							.equalsIgnoreCase(AutomaticParallelizationPreTransformationHandler.HANDLER_NAME)) {
						throw new OdysseusScriptException(
								"Automatic parallelization preTransoformationHandler already initialized");
					}
				}
			}
		}
		if (exists == null) {
			exists = new PreTransformationHandlerParameter();
			settings.add(exists);
		}
		List<Pair<String, String>> parameterList = new ArrayList<Pair<String, String>>();
		exists.add(AutomaticParallelizationPreTransformationHandler.HANDLER_NAME, parameterList);

	}

}
