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
package de.uniol.inf.is.odysseus.parallelization.intraoperator.keyword;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValueElement;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.constants.IntraOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter.IntraOperatorParallelizationKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.preexecution.IntraOperatorPreExecutionHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

/**
 * keyword class for INTRAOPERATOR parallelization. this keyword is used to
 * define operator specific configuration for parallelization (e.g. custom
 * degree for only this operator)
 * 
 * @author ChrisToenjesDeye
 *
 */
public class IntraOperatorParallelizationPreParserKeyword extends
		AbstractPreParserKeyword {
	public static final String KEYWORD = "INTRAOPERATOR";

	private static final Logger LOG = LoggerFactory
			.getLogger(IntraOperatorParallelizationPreParserKeyword.class);

	private PreParserKeywordParameterHelper<IntraOperatorParallelizationKeywordParameter> parameterHelper;

	/**
	 * validates the parameters defined in this keyword
	 */
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		// validate parameter string
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorParallelizationKeywordParameter.class);
		parameterHelper.validateParameterString(parameter);

	}

	/**
	 * do execute the keyword. here parameters are parsed and adds custom config
	 * to existing intra operator configuration
	 */
	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		ParallelIntraOperatorSetting intraOperatorSetting = getIntraOperatorParallelizationSettingIfExists(settings);

		// parse and validate input parameters
		Map<IKeywordParameter, String> result = parameterHelper
				.parse(parameter);
		List<String> operatorIds = parseOperatorIds(result
				.get(IntraOperatorParallelizationKeywordParameter.OPERATORID));
		int individualDegree = parseDegree(
				result.get(IntraOperatorParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION),
				intraOperatorSetting.getValue());
		int individualBuffersize = parseBuffersize(
				result.get(IntraOperatorParallelizationKeywordParameter.BUFFERSIZE),
				intraOperatorSetting.getValue());

		// create individual settings for each operator id
		for (String operatorId : operatorIds) {
			ParallelIntraOperatorSettingValueElement individualSettings = new ParallelIntraOperatorSettingValueElement(
					individualDegree, individualBuffersize);
			intraOperatorSetting.getValue().addIndividualSettings(operatorId,
					individualSettings);
		}

		return null;
	}

	/**
	 * Parse and validate value for buffersize. This value is used inside of
	 * intra parallelized operators to buffer input stream. Returns the default
	 * value if buffersize is not set or AUTO constant is defined
	 * 
	 * @param parameter
	 *            value for buffersize
	 * @return
	 * @throws OdysseusScriptException
	 */
	private int parseBuffersize(String bufferString,
			ParallelIntraOperatorSettingValue parallelIntraOperatorSettingValue)
			throws OdysseusScriptException {
		if (bufferString == null) {
			return IntraOperatorParallelizationConstants.DEFAULT_BUFFERSIZE;
		} else {
			try {
				int buffersize = Integer.parseInt(bufferString);
				if (buffersize > 0) {
					return buffersize;
				} else {
					throw new OdysseusScriptException(
							"Value for buffersize is not valid. Value need to be greater 0.");
				}
			} catch (NumberFormatException e) {
				if (bufferString
						.equalsIgnoreCase(IntraOperatorParallelizationConstants.AUTO)) {
					return IntraOperatorParallelizationConstants.DEFAULT_BUFFERSIZE;
				} else if (bufferString
						.equalsIgnoreCase(IntraOperatorParallelizationConstants.GLOBAL)) {
					return parallelIntraOperatorSettingValue
							.getGlobalBuffersize();
				}
				throw new OdysseusScriptException(
						"Value '"
								+ bufferString
								+ "' for buffersize is not valid. Use positive integer values or constants 'AUTO' or 'GLOBAL'");
			}
		}
	}

	/**
	 * parse and validate value for degree parameter. Only positive integers are
	 * allowed
	 * 
	 * @param degreeOfParallelizationString
	 * @param parallelIntraOperatorSettingValue
	 * @return
	 * @throws OdysseusScriptException
	 */
	private int parseDegree(String degreeOfParallelizationString,
			ParallelIntraOperatorSettingValue parallelIntraOperatorSettingValue)
			throws OdysseusScriptException {

		try {
			int degreeOfParallelization = Integer
					.parseInt(degreeOfParallelizationString);
			if (degreeOfParallelization < 1) {
				throw new OdysseusScriptException(
						"Value for degreeOfParallelization is not valid. Only positive integer values >= 1 is allowed.");
			}
			if (degreeOfParallelization > PerformanceDetectionHelper
					.getNumberOfCores()) {
				LOG.warn("Degree of parallelization is greater than available cores");
			}
			return degreeOfParallelization;
		} catch (NumberFormatException e) {
			// degree of parallelization is no integer, so check if it is
			// constant auto or global
			if (degreeOfParallelizationString
					.equalsIgnoreCase(IntraOperatorParallelizationConstants.AUTO)) {
				return PerformanceDetectionHelper.getNumberOfCores();
			} else if (degreeOfParallelizationString
					.equalsIgnoreCase(IntraOperatorParallelizationConstants.GLOBAL)) {
				return parallelIntraOperatorSettingValue.getGlobalDegree();
			} else {
				throw new OdysseusScriptException(
						"Value '"
								+ degreeOfParallelizationString
								+ "' for degree is invalid. Use positive integer values or constants 'AUTO' or 'GLOBAL'");
			}
		}
	}

	/**
	 * Parses the operator ids from parameter. if more than one operator id is
	 * defined, the ids need to be seperated with a comma
	 * 
	 * @param operatorIdString
	 * @return
	 * @throws OdysseusScriptException
	 */
	private List<String> parseOperatorIds(String operatorIdString)
			throws OdysseusScriptException {
		if (operatorIdString.isEmpty()) {
			throw new OdysseusScriptException(
					"Value for operatorId is not valid");
		}
		List<String> operatorIds = new ArrayList<String>();

		String[] splittedIds = operatorIdString.trim().split(",");
		for (int i = 0; i < splittedIds.length; i++) {
			operatorIds.add(splittedIds[i]);
		}

		return operatorIds;
	}

	/**
	 * gets the global setting value for intra operator parallelization. If the
	 * settings arent exist an error is shown, because it is not allowed to use
	 * this keyword without global #PARALLELIZATION keyword and type to intra
	 * operator
	 * 
	 * @param settings
	 * @return
	 * @throws OdysseusScriptException
	 */
	private ParallelIntraOperatorSetting getIntraOperatorParallelizationSettingIfExists(
			List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// check if #PARALLELIZATION keyword exists and type is set to
		// intra-operator
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(ParallelIntraOperatorSetting.class)) {
				return (ParallelIntraOperatorSetting) setting;
			}
		}

		throw new OdysseusScriptException(
				"#PARALLELIZATION keyword with type= "
						+ IntraOperatorPreExecutionHandler.TYPE
						+ " is missing or placed after #" + KEYWORD
						+ " keyword.");
	}

}
