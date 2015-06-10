package de.uniol.inf.is.odysseus.multithreaded.keyword;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.multithreaded.transform.IParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.multithreaded.transform.registry.ParallelizationPreTransformationHandlerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MultithreadedPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "MULTITHREADED";

	private static final Logger LOG = LoggerFactory
			.getLogger(MultithreadedPreParserKeyword.class);
	private static final int ATTRIBUTE_COUNT = 2;
	private static final String PATTERN = "<Parallelization-Type> <Degree of parallelization, or AUTO>";

	private int degreeOfParallelization = 0;

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Parameters for " + KEYWORD
					+ " are missing");
		}

		String[] splitted = parameter.trim().split(" ");

		if (splitted.length != ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " needs "
					+ ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		} else {
			String parallelizationType = splitted[0];
			if (!isValidType(parallelizationType)) {
				throw new OdysseusScriptException("");
			}

			String degreeOfParallelization = splitted[1];
			if (!isValidDegree(degreeOfParallelization)) {
				throw new OdysseusScriptException("");
			}
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		String[] splitted = parameter.trim().split(" ");
		
		
		IParallelizationPreTransformationHandler handler = ParallelizationPreTransformationHandlerRegistry
				.getPreTransformationHandlerByType(splitted[0]);
		if (handler != null) {
			PreTransformationHandlerParameter newHandlerParameter = handler
					.createHandlerParameter(degreeOfParallelization);
			
			boolean parameterAlreadyAdded = false;
			for( IQueryBuildSetting<?> setting : settings ) {
				if( setting.getClass().equals(PreTransformationHandlerParameter.class)) {
					PreTransformationHandlerParameter existingHandlerParameter = (PreTransformationHandlerParameter) setting;
					for (HandlerParameterPair newPair : newHandlerParameter.getPairs()) {
						for (HandlerParameterPair existingPair : existingHandlerParameter.getPairs()) {
							if (newPair.name.equalsIgnoreCase(existingPair.name)){
								LOG.warn("Duplicate definition for multithreaded keyword. Please use this keyword only once. Only first occurrence is considered.");
								return null;
							}
						}
						existingHandlerParameter.add(newPair.name, newPair.parameters);
					}
					parameterAlreadyAdded = true;
					break;
				}
			}
			
			if(!parameterAlreadyAdded){
				settings.add(newHandlerParameter);
			}
		} else {
			throw new OdysseusScriptException();
		}

		return null;
	}

	private boolean isValidType(String parallelizationType) {
		return ParallelizationPreTransformationHandlerRegistry
				.getValidTypes().contains(
						parallelizationType.toLowerCase()) ? true : false;
	}

	private boolean isValidDegree(String degreeOfParallelization)
			throws OdysseusScriptException {
		try {
			int integerDegree = Integer.parseInt(degreeOfParallelization);

			// degree need to be greater than 1,
			if (integerDegree > 1) {
				this.degreeOfParallelization = integerDegree;
				if (this.degreeOfParallelization > PerformanceDetectionHelper
						.getNumberOfCores()) {
					LOG.warn("Degree of parallelization is greater than available cores");
				}

				return true;
			}
		} catch (NumberFormatException e) {
			// if there is no degree, maybe auto detection of degree is enabled
			if (degreeOfParallelization.equalsIgnoreCase("AUTO")) {
				int availableCores = PerformanceDetectionHelper
						.getNumberOfCores();
				if (availableCores <= 1) {
					throw new OdysseusScriptException("");
				}

				LOG.info("Number of detected cores is " + availableCores
						+ ". Degree of parallelization is set to this value.");
				this.degreeOfParallelization = availableCores;
				return true;
			}
		}

		return false;
	}

}
