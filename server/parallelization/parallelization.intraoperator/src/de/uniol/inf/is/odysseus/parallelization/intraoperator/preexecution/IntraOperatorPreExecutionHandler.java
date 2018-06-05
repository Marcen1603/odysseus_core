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
package de.uniol.inf.is.odysseus.parallelization.intraoperator.preexecution;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.constants.IntraOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter.IntraOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.preexecution.AbstractParallelizationPreExecutionHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

/**
 * Pre Execution handler for intra operator parallelization. This executors
 * allows easy extension of different parallelization types. this executors are
 * called from execute methods of #PARALLELIZATION keyword. This preExecution
 * handler creates setting, which is used in transformation rules of operators
 * to transform logical operators in threaded physical operators
 * 
 * @author ChrisToenjesDeye
 *
 */
public class IntraOperatorPreExecutionHandler extends
		AbstractParallelizationPreExecutionHandler {

	public static final String TYPE = "INTRA_OPERATOR";

	private static final InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(IntraOperatorPreExecutionHandler.class);

	private PreParserKeywordParameterHelper<IntraOperatorGlobalKeywordParameter> parameterHelper;

	private int globalDegreeOfParallelization = 0;
	private int globalBuffersize = 0;

	public IntraOperatorPreExecutionHandler() {
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorGlobalKeywordParameter.class);
	}

	/**
	 * Validates the parameter string based on the parallelization type (here
	 * intra-operator)
	 * 
	 * @param parameterString
	 */
	@Override
	public void validateParameters(String parameterString) {
		parameterHelper.validateParameterString(parameterString);
	}

	/**
	 * does the execution for intra-operator parallelization. creates setting,
	 * which is used in transformation rules of operators to transform logical
	 * operators in threaded physical operators
	 * 
	 * @param parameterString
	 * @param settings
	 * @throws OdysseusScriptException
	 */
	@Override
	public void preExecute(String parameterString,
			List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// parse given parameters
		Map<IKeywordParameter, String> result = parameterHelper
				.parse(parameterString);

		// process parameters
		processDegreeParameter(result
				.get(IntraOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION));

		processBuffersizeParameter(result
				.get(IntraOperatorGlobalKeywordParameter.BUFFERSIZE));

		checkIfIntraOperatorSettingAlreadyExists(settings);

		// create global settings for intra operator parallelization. if only
		// this setting is set and no custom settings are defined in odysseus
		// script via #INTRAOPERATOR keyword, all possible operators use a
		// threaded physical operator
		ParallelIntraOperatorSettingValue value = new ParallelIntraOperatorSettingValue(
				globalDegreeOfParallelization, globalBuffersize);
		ParallelIntraOperatorSetting intraOperatorSetting = new ParallelIntraOperatorSetting(
				value);
		settings.add(intraOperatorSetting);
	}

	/**
	 * validate and parse the buffersize parameter. this value is needed for
	 * buffer inside of threaded physical operators. Uses a default value if
	 * nothing is set or constant AUTO is used
	 * 
	 * @param string
	 * @throws OdysseusScriptException
	 */
	private void processBuffersizeParameter(String string)
			throws OdysseusScriptException {
		if (string == null || string.equalsIgnoreCase("AUTO")) {
			globalBuffersize = IntraOperatorParallelizationConstants.DEFAULT_BUFFERSIZE;
		} else {
			try {
				int buffersize = Integer.parseInt(string);
				if (buffersize > 0) {
					globalBuffersize = buffersize;
				} else {
					throw new OdysseusScriptException(
							"Value for buffersize is not valid. Value need to be greater 0.");
				}
			} catch (NumberFormatException e) {
				throw new OdysseusScriptException(
						"Value for buffersize is not valid. Degree is no integer.");
			}
		}
	}

	/**
	 * checks if this keyword with intra-operator type is only once defined.
	 * 
	 * @param settings
	 * @throws OdysseusScriptException
	 */
	private void checkIfIntraOperatorSettingAlreadyExists(
			List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// get parameter from settings or create new one if not exists
		for (IQueryBuildSetting<?> setting : settings) {
			if (setting.getClass().equals(ParallelIntraOperatorSetting.class)) {
				throw new OdysseusScriptException("Duplicate definition of "
						+ ParallelizationPreParserKeyword.KEYWORD
						+ " with type " + IntraOperatorPreExecutionHandler.TYPE);
			}
		}
	}

	/**
	 * returns the name for this pre execution handler
	 * 
	 * @return the type
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * processes the degree parameter. only positive integers and constant AUTO
	 * are allowed. if AUTO is used, the number of cores for this instance is
	 * detected
	 * 
	 * @param degreeOfParallelization
	 * @throws OdysseusScriptException
	 */
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
				this.globalDegreeOfParallelization = availableCores;
			}
		}
	}
}
