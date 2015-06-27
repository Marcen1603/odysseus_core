package de.uniol.inf.is.odysseus.multithreaded.keyword;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.uniol.inf.is.odysseus.multithreaded.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.multithreaded.keyword.helper.IKeywordParameter;
import de.uniol.inf.is.odysseus.multithreaded.keyword.helper.PreParserKeywordParameterHelper;
import de.uniol.inf.is.odysseus.multithreaded.transform.IParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.multithreaded.transform.registry.ParallelizationPreTransformationHandlerRegistry;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class ParallelizationPreParserKeyword extends AbstractPreParserKeyword {
	public static final String KEYWORD = "PARALLELIZATION";
	public static final int AUTO_BUFFER_SIZE = 10000000;

	private static final InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(ParallelizationPreParserKeyword.class);

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = AUTO_BUFFER_SIZE;
	private boolean allowOptimization = true;

	private PreParserKeywordParameterHelper parameterHelper;

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

		List<IKeywordParameter> parameters = new ArrayList<IKeywordParameter>();
		List<ParallelizationKeywordParameter> asList = Arrays
				.asList(ParallelizationKeywordParameter.values());
		for (ParallelizationKeywordParameter parallelizationKeywordParameter : asList) {
			parameters.add(parallelizationKeywordParameter);
		}

		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(parameters);
		parameterHelper.validateParameterString(parameter);

		// split of parameters on whitespaces
		String[] splitted = parameter.trim().split(" ");

		// check if parallelization type exists
		String parallelizationType = splitted[0];
		if (!isValidType(parallelizationType)) {
			throw new OdysseusScriptException(
					"ParallelizationPreTransformationHandler with name "
							+ splitted[0]
							+ " not exists. Valid values are: "
							+ ParallelizationPreTransformationHandlerRegistry
									.getValidTypes());
		}

		// validate degree of parallelization
		String degreeOfParallelization = splitted[1];
		if (!isValidDegree(degreeOfParallelization)) {
			throw new OdysseusScriptException(
					"Value for degreeOfParallelization is not valid. Only positive integer values >= 1 or constant AUTO is allowed.");
		}

		// validate buffersize if set
		if (splitted.length >= 3) {
			String buffersizeString = splitted[2];
			if (!isValidBuffersize(buffersizeString)) {
				throw new OdysseusScriptException(
						"Value for buffersize is not valid. Only positive integer values or AUTO is allowed.");
			}
		}

		// validate optimization selection
		if (splitted.length == 4) {
			String optimizationString = splitted[3];
			if (!optimizationString.equalsIgnoreCase("true")
					&& !optimizationString.equalsIgnoreCase("false")) {
				throw new OdysseusScriptException(
						"Value for alowOptimization is invalid. Valid values are true or false.");
			} else {
				allowOptimization = Boolean.parseBoolean(optimizationString);
			}
		}

	}

	private boolean isValidBuffersize(String buffersizeString) {
		try {
			globalBufferSize = Integer.parseInt(buffersizeString);
			if (globalBufferSize < 1) {
				return false;
			}
		} catch (NumberFormatException e) {
			if (buffersizeString.equalsIgnoreCase("AUTO")) {
				globalBufferSize = AUTO_BUFFER_SIZE;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		// split of parameters on whitespaces
		String[] splitted = parameter.trim().split(" ");

		// get IParallelizationPreTransformationHandler by name
		IParallelizationPreTransformationHandler handler = ParallelizationPreTransformationHandlerRegistry
				.getPreTransformationHandlerByType(splitted[0]);
		if (handler != null) {
			// if handler exists
			PreTransformationHandlerParameter newHandlerParameter = handler
					.createHandlerParameter(globalDegreeOfParallelization,
							globalBufferSize, allowOptimization);

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
							if (newPair.name
									.equalsIgnoreCase(existingPair.name)) {
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
		} else {
			throw new OdysseusScriptException(
					"ParallelizationPreTransformationHandler with name "
							+ splitted[0]
							+ " not exists. Valid values are: "
							+ ParallelizationPreTransformationHandlerRegistry
									.getValidTypes());
		}

		return null;
	}

	private boolean isValidType(String parallelizationType) {
		return ParallelizationPreTransformationHandlerRegistry.getValidTypes()
				.contains(parallelizationType.toLowerCase()) ? true : false;
	}

	private boolean isValidDegree(String degreeOfParallelization)
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

				return true;
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
				return true;
			}
		}

		return false;
	}

}
