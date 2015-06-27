package de.uniol.inf.is.odysseus.multithreaded.interoperator.keyword;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.uniol.inf.is.odysseus.multithreaded.helper.FragmentationTypeHelper;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorParameter;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.parameter.MultithreadedOperatorSettings;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.strategy.registry.MultithreadedTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.transform.InterOperatorParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class InterOperatorParallelizationPreParserKeyword extends
		AbstractPreParserKeyword {

	private static final String PATTERN_PAIRS = "((([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))[,])"
			+ "*(([a-zA-Z0-9_]+)|([(][a-zA-Z0-9_]+([:][a-zA-Z0-9_]+)+[)]))";
	private static final String PATTERN_WITH_ATTRIBUTESNAMES = "([(][a-zA-Z0-9_]+[=]"+PATTERN_PAIRS+"[)])([\\s][(][a-zA-Z0-9_]+[=]"+PATTERN_PAIRS+"[)])*";
	private static final String PATTERN_WITHOUT_ATTRIBUTENAMES = "("+PATTERN_PAIRS+")([\\s]"+PATTERN_PAIRS+")*";
	private static final String PATTERN_KEYWORD = PATTERN_WITH_ATTRIBUTESNAMES+"|"+PATTERN_WITHOUT_ATTRIBUTENAMES;

	public enum DegreeOfParalleizationConstants {
		USERDEFINED, GLOBAL, AUTO;

		public static DegreeOfParalleizationConstants getConstantByName(
				String name) {
			for (DegreeOfParalleizationConstants parameter : DegreeOfParalleizationConstants
					.values()) {
				if (parameter.name().equalsIgnoreCase(name)) {
					return parameter;
				}
			}
			return null;
		}
	}

	public enum BufferSizeConstants {
		USERDEFINED, GLOBAL, AUTO;

		public static BufferSizeConstants getConstantByName(String name) {
			for (BufferSizeConstants parameter : BufferSizeConstants.values()) {
				if (parameter.name().equalsIgnoreCase(name)) {
					return parameter;
				}
			}
			return null;
		}
	}

	public static final String KEYWORD = "INTEROPERATORPARALLELIZATION";

	private static final Logger LOG = LoggerFactory
			.getLogger(InterOperatorParallelizationPreParserKeyword.class);
	private static final int MIN_ATTRIBUTE_COUNT = 3;
	private static final int MAX_ATTRIBUTE_COUNT = 5;
	private static final String PATTERN = "<1..n Unique operator Ids or Operator-Pairs (startId:endId)> "
			+ "<degree of parallelization or GLOBAL or AUTO> "
			+ "<buffer-size or GLOBAL or AUTO>"
			+ "<multithreading-strategy (optional)> "
			+ "<fragmentation-type e.g. round robin (optional)> ";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Parameters for " + KEYWORD
					+ " are missing");
		}

		//Pattern.compile(PATTERN_COMPLETE);
		if (!parameter.matches(PATTERN_KEYWORD)){
			throw new OdysseusScriptException("Parameters don't matches pattern");
		}
		
		String[] splitted = parameter.trim().split(" ");

		if (splitted.length < MIN_ATTRIBUTE_COUNT
				|| splitted.length > MAX_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " requires at least "
					+ MIN_ATTRIBUTE_COUNT + " and maximum "
					+ MAX_ATTRIBUTE_COUNT + "attributes: " + PATTERN + "!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		checkIfParallelizationKeywordExists(settings);

		// split parameters on whitespaces
		String[] keywordParameter = parameter.trim().split(" ");

		List<MultithreadedOperatorSettings> operatorSettings = createOperatorSettingsFromIds(keywordParameter);

		for (MultithreadedOperatorSettings operatorSetting : operatorSettings) {

			addParallelizationDegree(keywordParameter, operatorSetting);

			addBufferSize(keywordParameter, operatorSetting);

			addStrategy(keywordParameter, operatorSetting);

			addFragmentationType(keywordParameter, operatorSetting);
		}

		MultithreadedOperatorParameter mtOperatorParameter = getMultithreadedOperatorParameter(settings);

		addSettingsToParameter(operatorSettings, mtOperatorParameter);

		return null;
	}

	private List<MultithreadedOperatorSettings> createOperatorSettingsFromIds(
			String[] keywordParameter) throws OdysseusScriptException {
		// create operator settings
		List<MultithreadedOperatorSettings> operatorSettings = new ArrayList<MultithreadedOperatorSettings>();

		// 1. parameter: operatorId's
		if (keywordParameter[0].contains("(")
				&& keywordParameter[0].contains(")")) {
			String[] splittedOperatorIDs = keywordParameter[0].trim()
					.split(",");
			for (int i = 0; i < splittedOperatorIDs.length; i++) {
				String currentId = splittedOperatorIDs[i].trim();
				if (currentId.startsWith("(") && currentId.endsWith(")")) {
					// we have an id pair, split it on :
					currentId = currentId.substring(1, currentId.length() - 1);
					String[] currentIdPair = currentId.split(":");
					if (currentIdPair.length == 2) {
						// (StartParallelizationId:EndParallelizationId)
						MultithreadedOperatorSettings settingsForId = new MultithreadedOperatorSettings();
						settingsForId
								.setStartParallelizationId(currentIdPair[0]);
						settingsForId.setEndParallelizationId(currentIdPair[1]);
						operatorSettings.add(settingsForId);
					} else if (currentIdPair.length == 3) {
						// (StartParallelizationId:EndParallelizationId:AssureSemanticCorrectness)
						MultithreadedOperatorSettings settingsForId = new MultithreadedOperatorSettings();
						settingsForId
								.setStartParallelizationId(currentIdPair[0]);
						settingsForId.setEndParallelizationId(currentIdPair[1]);

						if (!currentIdPair[2].equalsIgnoreCase("true")
								&& !currentIdPair[2].equalsIgnoreCase("false")) {
							throw new OdysseusScriptException(
									"Value for AssureSemanticCorrectness is invalid. Valid values are: true or false");
						} else {
							settingsForId.setAssureSemanticCorrectness(Boolean
									.parseBoolean(currentIdPair[2]));
						}
						operatorSettings.add(settingsForId);
					} else {
						throw new OdysseusScriptException(
								"Definition of ids has an invalid structure");
					}
				} else if (!currentId.contains("(") && !currentId.contains(")")) {
					// we have an single id
					MultithreadedOperatorSettings settingsForId = new MultithreadedOperatorSettings();
					settingsForId.setStartParallelizationId(currentId);
					operatorSettings.add(settingsForId);
				} else {
					throw new OdysseusScriptException(
							"Definition of ids has an invalid structure");
				}
			}

		} else if (!keywordParameter[0].contains("(")
				&& !keywordParameter[0].contains(")")) {
			String[] splittedOperatorIDs = keywordParameter[0].trim()
					.split(",");
			for (String operatorId : Arrays.asList(splittedOperatorIDs)) {
				MultithreadedOperatorSettings settingsForId = new MultithreadedOperatorSettings();
				settingsForId.setStartParallelizationId(operatorId);
				operatorSettings.add(settingsForId);
			}
		} else {
			throw new OdysseusScriptException(
					"Definition of ids has an invalid structure");
		}
		return operatorSettings;
	}

	private void addSettingsToParameter(
			List<MultithreadedOperatorSettings> operatorSettings,
			MultithreadedOperatorParameter mtOperatorParameter)
			throws OdysseusScriptException {
		// check if settings for one of the given operatorIds already exists
		for (MultithreadedOperatorSettings operatorSetting : operatorSettings) {
			if (mtOperatorParameter.getOperatorIds().contains(
					operatorSetting.getStartParallelizationId())) {
				throw new OdysseusScriptException(
						"Multiple definition for operator with id: "
								+ operatorSetting.getStartParallelizationId());
			} else {
				mtOperatorParameter.addSettingsForOperator(
						operatorSetting.getStartParallelizationId(),
						operatorSetting);
			}
		}
	}

	private MultithreadedOperatorParameter getMultithreadedOperatorParameter(
			List<IQueryBuildSetting<?>> settings) {
		// get parameter from settings or create new one if not exists
		MultithreadedOperatorParameter mtOperatorParameter = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(MultithreadedOperatorParameter.class)) {
				mtOperatorParameter = (MultithreadedOperatorParameter) setting;
			}
		}
		if (mtOperatorParameter == null) {
			mtOperatorParameter = new MultithreadedOperatorParameter();
			settings.add(mtOperatorParameter);
		}
		return mtOperatorParameter;
	}

	private void checkIfParallelizationKeywordExists(
			List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// check if #PARALLELIZATION keyword exists and type is set to
		// inter-operator
		boolean parallelizationHandlerExists = false;

		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(
					PreTransformationHandlerParameter.class)) {
				PreTransformationHandlerParameter existingHandlerParameter = (PreTransformationHandlerParameter) setting;
				for (HandlerParameterPair handlerParameterPair : existingHandlerParameter
						.getPairs()) {
					if (handlerParameterPair.name
							.equalsIgnoreCase(InterOperatorParallelizationPreTransformationHandler.HANDLER_NAME)) {
						parallelizationHandlerExists = true;
					}
				}

			}
		}
		if (!parallelizationHandlerExists) {
			throw new OdysseusScriptException(
					"#PARALLELIZATION keyword is missing or placed after #INTEROPERATORPARALLELIZATION keyword.");
		}
	}

	private void addFragmentationType(String[] keywordParameter,
			MultithreadedOperatorSettings operatorSetting)
			throws OdysseusScriptException {
		// 5. parameter (optional): fragmentation-type
		if (keywordParameter.length == 5) {
			String dataFragmentation = keywordParameter[4];
			if (FragmentationTypeHelper
					.isValidFragmentationType(dataFragmentation)) {
				operatorSetting.setFragementationType(dataFragmentation);
			} else {
				throw new OdysseusScriptException(
						"Value for fragmentation type is not valid");
			}

		}
	}

	private void addStrategy(String[] keywordParameter,
			MultithreadedOperatorSettings operatorSetting)
			throws OdysseusScriptException {
		// 4. parameter (optional): multithreading-strategy
		if (keywordParameter.length >= 4) {
			String strategyName = keywordParameter[3];
			if (MultithreadedTransformationStrategyRegistry
					.isValidStrategyName(strategyName)) {
				operatorSetting.setMultithreadingStrategy(strategyName);
			} else {
				throw new OdysseusScriptException(
						"Value for Tranformation strategy is not valid");
			}
		}
	}

	private void addBufferSize(String[] keywordParameter,
			MultithreadedOperatorSettings operatorSetting)
			throws OdysseusScriptException {
		// 3. parameter: buffer-size
		String buffersizeString = keywordParameter[2];
		try {
			int buffersize = Integer.parseInt(buffersizeString);
			operatorSetting.setBufferSize(buffersize);
		} catch (NumberFormatException e) {
			LOG.debug("Buffersize is no integer. Try to determine constant value");
			BufferSizeConstants buffersizeConstant = BufferSizeConstants
					.getConstantByName(buffersizeString);
			if (buffersizeConstant != null
					&& buffersizeConstant != BufferSizeConstants.USERDEFINED) {
				operatorSetting.setBufferSizeConstant(buffersizeConstant);
			} else {
				throw new OdysseusScriptException(
						"Buffersize is not an integer or an valid constant: ");
			}
		}
	}

	private void addParallelizationDegree(String[] keywordParameter,
			MultithreadedOperatorSettings operatorSetting)
			throws OdysseusScriptException {
		// 2. parameter: degree of parallelization for defined operators
		String degreeOfParallelizationString = keywordParameter[1];
		try {
			int degreeOfParallelization = Integer
					.parseInt(degreeOfParallelizationString);
			if (degreeOfParallelization < 1) {
				throw new OdysseusScriptException(
						"Value for degreeOfParallelization is not valid. Only positive integer values >= 1 or constant AUTO is allowed.");
			}
			if (degreeOfParallelization > PerformanceDetectionHelper
					.getNumberOfCores()) {
				LOG.warn("Degree of parallelization is greater than available cores");
			}
			operatorSetting.setDegreeOfParallelization(degreeOfParallelization);
		} catch (NumberFormatException e) {
			LOG.debug("Degree is no integer. Try to determine constant value");
			DegreeOfParalleizationConstants degreeOfParalleizationConstant = DegreeOfParalleizationConstants
					.getConstantByName(degreeOfParallelizationString);
			if (degreeOfParalleizationConstant != null
					&& degreeOfParalleizationConstant != DegreeOfParalleizationConstants.USERDEFINED) {
				operatorSetting
						.setDegreeConstant(degreeOfParalleizationConstant);
			} else {
				throw new OdysseusScriptException(
						"DegreeOfParallelization is not an integer or an valid constant: ");
			}
		}
	}
}
