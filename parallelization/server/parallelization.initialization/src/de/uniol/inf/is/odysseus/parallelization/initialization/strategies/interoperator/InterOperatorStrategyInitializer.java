/**
 * 
 */
package de.uniol.inf.is.odysseus.parallelization.initialization.strategies.interoperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.AbstractParallelizationStrategyInitializer;
import de.uniol.inf.is.odysseus.parallelization.initialization.strategies.intraoperator.IntraOperatorStrategyInitializer;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelInterOperatorSetting;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.InterOperatorParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * @author Dennis Nowak
 *
 */
public class InterOperatorStrategyInitializer extends AbstractParallelizationStrategyInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(IntraOperatorStrategyInitializer.class);

	private static int DEFAULT_DEGREE = 0;
	private static int DEFAULT_BUFFERSIZE = 0;
	private static boolean DEFAULT_ALLOW_OPTIMIZATION = false;
	//FIXME is also used for individual configurations
	private static boolean DEFUALT_USE_THREADEDBUFFER = true;

	@Override
	public void initialize(List<IQueryBuildSetting<?>> settings) throws OdysseusScriptException {
		PreTransformationHandlerParameter newPreTransformationHandler = createPreTransformationHandlerParameter();
		settingAlreadyExists(settings, newPreTransformationHandler);
		PreTransformationHandlerParameter exists = null;
		ParallelInterOperatorSetting exists2 = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(PreTransformationHandlerParameter.class)) {
				exists = (PreTransformationHandlerParameter) setting;
			} else if (setting.getClass().equals(ParallelInterOperatorSetting.class)) {
				exists2 = (ParallelInterOperatorSetting) setting;
			}
		}
		if (exists == null) {
			settings.add(newPreTransformationHandler);
		} else {
			for (HandlerParameterPair newPair : newPreTransformationHandler.getPairs()) {
				exists.add(newPair.name, newPair.parameters);
			}
		}
		if (exists2 == null) {
			settings.add(new ParallelInterOperatorSetting());
		}
		LOG.debug("Added initial settings for automatic Inter_Operator parallelization");

	}

	private PreTransformationHandlerParameter createPreTransformationHandlerParameter() {
		List<Pair<String, String>> parameterList = new ArrayList<Pair<String, String>>();

		Pair<String, String> degree = new Pair<String, String>(
				InterOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION.name(), String.valueOf(DEFAULT_DEGREE));
		parameterList.add(degree);
		Pair<String, String> buffer = new Pair<String, String>(InterOperatorGlobalKeywordParameter.BUFFERSIZE.name(),
				String.valueOf(DEFAULT_BUFFERSIZE));
		parameterList.add(buffer);
		Pair<String, String> optimization = new Pair<String, String>(
				InterOperatorGlobalKeywordParameter.OPTIMIZATION.name(), String.valueOf(DEFAULT_ALLOW_OPTIMIZATION));
		parameterList.add(optimization);
		Pair<String, String> threadedBuffer = new Pair<String, String>(
				InterOperatorGlobalKeywordParameter.THREADEDBUFFER.name(), String.valueOf(DEFUALT_USE_THREADEDBUFFER));
		parameterList.add(threadedBuffer);

		PreTransformationHandlerParameter newParameter = new PreTransformationHandlerParameter();
		newParameter.add(InterOperatorParallelizationPreTransformationHandler.HANDLER_NAME, parameterList);
		return newParameter;
	}

	private void settingAlreadyExists(List<IQueryBuildSetting<?>> settings,
			PreTransformationHandlerParameter newParameter) throws OdysseusScriptException {
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(PreTransformationHandlerParameter.class)) {
				PreTransformationHandlerParameter existing = (PreTransformationHandlerParameter) setting;
				for (HandlerParameterPair newPair : newParameter.getPairs()) {
					for (HandlerParameterPair existingPair : existing.getPairs()) {
						if (newPair.name.equalsIgnoreCase(existingPair.name)) {
							throw new OdysseusScriptException(
									"AUTOMATIC parallelization initializes other parallelization types. Do not use #PARALLELIZATION INTER_OPERATOR.");
						}
					}
				}
			} else if (setting.getClass().equals(ParallelInterOperatorSetting.class)) {
				ParallelInterOperatorSetting parallelInterOperatorSetting = (ParallelInterOperatorSetting) setting;
				if (!parallelInterOperatorSetting.getOperatorIds().isEmpty()) {
					throw new OdysseusScriptException(
							"There are already individual settings for INTER_OPERATOR parallelization");
				}
			}
		}

	}

}
