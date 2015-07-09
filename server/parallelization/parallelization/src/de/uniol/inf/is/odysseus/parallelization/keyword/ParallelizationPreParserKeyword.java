package de.uniol.inf.is.odysseus.parallelization.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.parameter.ParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.transformationhandler.IParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.parallelization.transformationhandler.registry.ParallelizationPreTransformationHandlerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class ParallelizationPreParserKeyword extends AbstractPreParserKeyword {
	public static final String KEYWORD = "PARALLELIZATION";
	public static final int AUTO_BUFFER_SIZE = 10000000;

	private static final InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(ParallelizationPreParserKeyword.class);

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = AUTO_BUFFER_SIZE;
	private boolean allowOptimization = true;
	private boolean useThreadedBuffer = true;

	private PreParserKeywordParameterHelper<ParallelizationKeywordParameter> parameterHelper;

	@Override 
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(ParallelizationKeywordParameter.class);
		parameterHelper.validateParameterString(parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameterString, ISession caller, Context context)
			throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		Map<IKeywordParameter, String> result = parameterHelper
				.parse(parameterString);

		IParallelizationPreTransformationHandler handler = getParallelizationPreTransformationHandler(result);

		// process parameters
		processDegreeParameter(result
				.get(ParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION));
		processBuffersizeParameter(result
				.get(ParallelizationKeywordParameter.BUFFERSIZE));
		if (result.containsKey(ParallelizationKeywordParameter.OPTIMIZATION)) {
			processOptimizationValue(result
					.get(ParallelizationKeywordParameter.OPTIMIZATION));
		}
		if (result.containsKey(ParallelizationKeywordParameter.THREADEDBUFFER)){
			processUseThreadedBuffer(result.get(ParallelizationKeywordParameter.THREADEDBUFFER));
		}

		// create handler parameter for pretransformation
		createHandlerParameter(settings, handler);

		return null;
	}

	private void processUseThreadedBuffer(String useThreadedBuffer) throws OdysseusScriptException {
		if (!useThreadedBuffer.equalsIgnoreCase("true")
				&& !useThreadedBuffer.equalsIgnoreCase("false")) {
			throw new OdysseusScriptException(
					"Value for ThreadedBuffer is invalid. Valid values are true or false.");
		} else {
			this.useThreadedBuffer = Boolean.parseBoolean(useThreadedBuffer);
		}
	}

	private void processOptimizationValue(String optimization)
			throws OdysseusScriptException {
		if (!optimization.equalsIgnoreCase("true")
				&& !optimization.equalsIgnoreCase("false")) {
			throw new OdysseusScriptException(
					"Value for alowOptimization is invalid. Valid values are true or false.");
		} else {
			allowOptimization = Boolean.parseBoolean(optimization);
		}
	}

	private void createHandlerParameter(List<IQueryBuildSetting<?>> settings,
			IParallelizationPreTransformationHandler handler)
			throws OdysseusScriptException {
		// if handler exists
		PreTransformationHandlerParameter newHandlerParameter = handler
				.createHandlerParameter(globalDegreeOfParallelization,
						globalBufferSize, allowOptimization, useThreadedBuffer);

		boolean parameterAlreadyAdded = false;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(
					PreTransformationHandlerParameter.class)) {
				PreTransformationHandlerParameter existingHandlerParameter = (PreTransformationHandlerParameter) setting;
				for (HandlerParameterPair newPair : newHandlerParameter
						.getPairs()) {
					// check if parameter already exists
					for (HandlerParameterPair existingPair : existingHandlerParameter
							.getPairs()) {
						if (newPair.name.equalsIgnoreCase(existingPair.name)) {
							throw new OdysseusScriptException(
									"Duplicate definition for " + KEYWORD
											+ " keyword is not allowed");
						}
					}
					existingHandlerParameter.add(newPair.name,
							newPair.parameters);
				}
				parameterAlreadyAdded = true;
				break;
			}
		}

		if (!parameterAlreadyAdded) {
			settings.add(newHandlerParameter);
		}
	}

	private IParallelizationPreTransformationHandler getParallelizationPreTransformationHandler(
			Map<IKeywordParameter, String> result)
			throws OdysseusScriptException {
		// Get IParallelizationPreTransformationHandler by name
		String parallelizationType = result
				.get(ParallelizationKeywordParameter.PARALLELIZATION_TYPE);
		if (!isValidType(parallelizationType)) {
			throw new OdysseusScriptException(
					"ParallelizationPreTransformationHandler with name "
							+ parallelizationType
							+ " not exists. Valid values are: "
							+ ParallelizationPreTransformationHandlerRegistry
									.getValidTypes());
		}
		IParallelizationPreTransformationHandler handler = ParallelizationPreTransformationHandlerRegistry
				.getPreTransformationHandlerByType(parallelizationType);
		return handler;
	}

	private boolean isValidType(String parallelizationType) {
		return ParallelizationPreTransformationHandlerRegistry.getValidTypes()
				.contains(parallelizationType.toLowerCase()) ? true : false;
	}

	private void processDegreeParameter(String degreeOfParallelization)
			throws OdysseusScriptException {
		try {
			int integerDegree = Integer.parseInt(degreeOfParallelization);

			// degree need to be greater eq 1,
			if (integerDegree >= 1) {
				this.globalDegreeOfParallelization = integerDegree;
				if (this.globalDegreeOfParallelization > PerformanceDetectionHelper
						.getNumberOfCores()) {
					INFO_SERVICE
							.warning("Degree of parallelization is greater than available cores");
				}
			}
		} catch (NumberFormatException e) {
			// if there is no degree, maybe auto detection of degree is enabled
			if (degreeOfParallelization.equalsIgnoreCase("AUTO")) {
				int availableCores = PerformanceDetectionHelper
						.getNumberOfCores();
				if (availableCores <= 1) {
					throw new OdysseusScriptException(
							"AUTO detection of parallelization degree is not possible, because there is only on core detected.");
				}

				INFO_SERVICE.info("Number of detected cores is "
						+ availableCores
						+ ". Degree of parallelization is set to this value.");
				this.globalDegreeOfParallelization = availableCores;
			}
		}
	}

	private void processBuffersizeParameter(String buffersizeString)
			throws OdysseusScriptException {
		try {
			globalBufferSize = Integer.parseInt(buffersizeString);
			if (globalBufferSize < 1) {
				throw new OdysseusScriptException();
			}
		} catch (NumberFormatException e) {
			if (buffersizeString.equalsIgnoreCase("AUTO")) {
				globalBufferSize = AUTO_BUFFER_SIZE;
			} else {
				throw new OdysseusScriptException();
			}
		}
	}

}
