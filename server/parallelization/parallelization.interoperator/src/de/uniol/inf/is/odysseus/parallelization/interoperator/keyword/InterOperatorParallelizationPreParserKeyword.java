/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parallelization.interoperator.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.helper.FragmentationTypeHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelOperatorConfiguration;
import de.uniol.inf.is.odysseus.parallelization.interoperator.configuration.ParallelInterOperatorSetting;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.BufferSizeConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.DegreeOfParalleizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.InterOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.preexecution.InterOperatorPreExecutionHandler;
import de.uniol.inf.is.odysseus.parallelization.interoperator.strategy.registry.ParallelTransformationStrategyRegistry;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.InterOperatorParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class InterOperatorParallelizationPreParserKeyword extends
		AbstractPreParserKeyword {

	public static final String KEYWORD = "INTEROPERATOR";

	private static final Logger LOG = LoggerFactory
			.getLogger(InterOperatorParallelizationPreParserKeyword.class);
	private PreParserKeywordParameterHelper<InterOperatorParallelizationKeywordParameter> parameterHelper;

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(
						InterOperatorParallelizationKeywordParameter.class,
						InterOperatorParallelizationConstants.PATTERN_KEYWORD);
		parameterHelper.validateParameterString(parameter);
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		checkIfParallelizationKeywordExists(settings);

		Map<IKeywordParameter, String> result = parameterHelper
				.parse(parameter);
		List<ParallelOperatorConfiguration> operatorConfigurations = createOperatorSettingsFromIds(result
				.get(InterOperatorParallelizationKeywordParameter.OPERATORID));
		for (ParallelOperatorConfiguration operatorConfiguration : operatorConfigurations) {
			addParallelizationDegree(
					result.get(InterOperatorParallelizationKeywordParameter.DEGREE),
					operatorConfiguration);
			addBufferSize(
					result.get(InterOperatorParallelizationKeywordParameter.BUFFERSIZE),
					operatorConfiguration);
			addStrategy(result, operatorConfiguration);
			addFragmentationType(result, operatorConfiguration);
			addUseParallelOperators(result, operatorConfiguration);
		}

		ParallelInterOperatorSetting interOperatorSetting = getMultithreadedOperatorParameter(settings);
		addSettingsToParameter(operatorConfigurations, interOperatorSetting);

		return null;
	}

	private List<ParallelOperatorConfiguration> createOperatorSettingsFromIds(
			String operatorIds) throws OdysseusScriptException {
		// create operator settings
		List<ParallelOperatorConfiguration> operatorSettings = new ArrayList<ParallelOperatorConfiguration>();

		// 1. parameter: operatorId's
		if (operatorIds.contains("(") && operatorIds.contains(")")) {
			String[] splittedOperatorIDs = operatorIds.trim().split(",");
			for (int i = 0; i < splittedOperatorIDs.length; i++) {
				String currentId = splittedOperatorIDs[i].trim();
				if (currentId.startsWith("(") && currentId.endsWith(")")) {
					// we have an id pair, split it on :
					currentId = currentId.substring(1, currentId.length() - 1);
					String[] currentIdPair = currentId.split(":");
					if (currentIdPair.length == 2) {
						// (StartParallelizationId:EndParallelizationId)
						ParallelOperatorConfiguration settingsForId = new ParallelOperatorConfiguration();
						settingsForId
								.setStartParallelizationId(currentIdPair[0]);
						settingsForId.setEndParallelizationId(currentIdPair[1]);
						operatorSettings.add(settingsForId);
					} else if (currentIdPair.length == 3) {
						// (StartParallelizationId:EndParallelizationId:AssureSemanticCorrectness)
						ParallelOperatorConfiguration settingsForId = new ParallelOperatorConfiguration();
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
					ParallelOperatorConfiguration settingsForId = new ParallelOperatorConfiguration();
					settingsForId.setStartParallelizationId(currentId);
					operatorSettings.add(settingsForId);
				} else {
					throw new OdysseusScriptException(
							"Definition of ids has an invalid structure");
				}
			}

		} else if (!operatorIds.contains("(") && !operatorIds.contains(")")) {
			String[] splittedOperatorIDs = operatorIds.trim().split(",");
			for (String operatorId : Arrays.asList(splittedOperatorIDs)) {
				ParallelOperatorConfiguration settingsForId = new ParallelOperatorConfiguration();
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
			List<ParallelOperatorConfiguration> operatorSettings,
			ParallelInterOperatorSetting mtOperatorParameter)
			throws OdysseusScriptException {
		// check if settings for one of the given operatorIds already exists
		for (ParallelOperatorConfiguration operatorSetting : operatorSettings) {
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

	private ParallelInterOperatorSetting getMultithreadedOperatorParameter(
			List<IQueryBuildSetting<?>> settings) {
		// get parameter from settings or create new one if not exists
		ParallelInterOperatorSetting mtOperatorParameter = null;
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(ParallelInterOperatorSetting.class)) {
				mtOperatorParameter = (ParallelInterOperatorSetting) setting;
			}
		}
		if (mtOperatorParameter == null) {
			mtOperatorParameter = new ParallelInterOperatorSetting();
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
					"#PARALLELIZATION keyword with type= "
							+ InterOperatorPreExecutionHandler.TYPE
							+ " is missing or placed after #"+KEYWORD+" keyword.");
		}
	}

	private void addFragmentationType(Map<IKeywordParameter, String> result,
			ParallelOperatorConfiguration operatorSetting)
			throws OdysseusScriptException {
		// 5. parameter (optional): fragmentation-type
		if (result
				.containsKey(InterOperatorParallelizationKeywordParameter.FRAGMENTATION)) {
			String dataFragmentation = result
					.get(InterOperatorParallelizationKeywordParameter.FRAGMENTATION);
			if (FragmentationTypeHelper
					.isValidFragmentationType(dataFragmentation)) {
				operatorSetting.setFragementationType(dataFragmentation);
			} else {
				throw new OdysseusScriptException(
						"Value for fragmentation type is not valid");
			}

		}
	}

	private void addStrategy(Map<IKeywordParameter, String> result,
			ParallelOperatorConfiguration operatorSetting)
			throws OdysseusScriptException {
		// 4. parameter (optional): multithreading-strategy
		if (result
				.containsKey(InterOperatorParallelizationKeywordParameter.STRATEGY)) {
			String strategyName = result
					.get(InterOperatorParallelizationKeywordParameter.STRATEGY);
			if (ParallelTransformationStrategyRegistry
					.isValidStrategyName(strategyName)) {
				operatorSetting.setMultithreadingStrategy(strategyName);
			} else {
				throw new OdysseusScriptException(
						"Value for Transformation strategy is not valid");
			}
		}
	}

	private void addBufferSize(String buffersizeString,
			ParallelOperatorConfiguration operatorSetting)
			throws OdysseusScriptException {
		// 3. parameter: buffer-size
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

	private void addParallelizationDegree(String degreeOfParallelizationString,
			ParallelOperatorConfiguration operatorSetting)
			throws OdysseusScriptException {
		// 2. parameter: degree of parallelization for defined operators
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
	
	private void addUseParallelOperators(Map<IKeywordParameter, String> result,
			ParallelOperatorConfiguration operatorConfiguration) throws OdysseusScriptException {
		if (result.containsKey(InterOperatorParallelizationKeywordParameter.USEPARALLELOPERATOR)){
			String useParallelOperatorString = result.get(InterOperatorParallelizationKeywordParameter.USEPARALLELOPERATOR);
			// only true or false are allowed for parameter optimization
			if (!useParallelOperatorString.equalsIgnoreCase("true")
					&& !useParallelOperatorString.equalsIgnoreCase("false")) {
				throw new OdysseusScriptException(
						"Value for useParallelOperator is invalid. Valid values are true or false.");
			} else {
				operatorConfiguration.setUseParallelOperators(Boolean.parseBoolean(useParallelOperatorString));
			}
		}
	}
}
