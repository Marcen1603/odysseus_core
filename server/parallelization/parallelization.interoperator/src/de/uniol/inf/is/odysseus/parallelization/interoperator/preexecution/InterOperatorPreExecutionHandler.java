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
package de.uniol.inf.is.odysseus.parallelization.interoperator.preexecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.PreTransformationHandlerParameter.HandlerParameterPair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.interoperator.constants.InterOperatorParallelizationConstants;
import de.uniol.inf.is.odysseus.parallelization.interoperator.parameter.InterOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.interoperator.transform.InterOperatorParallelizationPreTransformationHandler;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.parallelization.preexecution.AbstractParallelizationPreExecutionHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

/**
 * Pre Execution handler for inter operator parallelization. This executors
 * allows easy extension of different parallelization types. this executors are
 * called from execute methods of #PARALLELIZATION keyword. This preExecution
 * handler creates preTransformationHandlerParameter which allows it to use use
 * PreTranformationHandlers to modifiy the given logical plan
 * 
 * @author ChrisToenjesDeye
 *
 */
public class InterOperatorPreExecutionHandler extends
		AbstractParallelizationPreExecutionHandler {
	public static final String TYPE = "INTER_OPERATOR";

	private PreParserKeywordParameterHelper<InterOperatorGlobalKeywordParameter> parameterHelper;
	private static final InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(InterOperatorPreExecutionHandler.class);

	private int globalDegreeOfParallelization = 0;
	private int globalBufferSize = InterOperatorParallelizationConstants.DEFAULT_BUFFER_SIZE;
	private boolean allowOptimization = true;
	private boolean useThreadedBuffer = true;

	public InterOperatorPreExecutionHandler() {
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(InterOperatorGlobalKeywordParameter.class);
	}

	/**
	 * Validates the parameter string based on the parallelization type (here
	 * inter-operator)
	 * 
	 * @param parameterString
	 */
	@Override
	public void validateParameters(String parameterString) {
		parameterHelper.validateParameterString(parameterString);
	}

	/**
	 * do the execution. Parse the existing parameter values and create the
	 * transformationHandlerParameter out of this values
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
				.get(InterOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION));
		processBuffersizeParameter(result
				.get(InterOperatorGlobalKeywordParameter.BUFFERSIZE));
		if (result
				.containsKey(InterOperatorGlobalKeywordParameter.OPTIMIZATION)) {
			processOptimizationValue(result
					.get(InterOperatorGlobalKeywordParameter.OPTIMIZATION));
		}
		if (result
				.containsKey(InterOperatorGlobalKeywordParameter.THREADEDBUFFER)) {
			processUseThreadedBuffer(result
					.get(InterOperatorGlobalKeywordParameter.THREADEDBUFFER));
		}

		// create handler parameter for pretransformation
		addPreTransformationHandlerParameter(settings);
	}

	/**
	 * returns the name of this parameter
	 */
	@Override
	public String getType() {
		return TYPE;
	}

	/**
	 * processes and validates the value for useThreadedBuffer parameter
	 * 
	 * @param useThreadedBuffer
	 * @throws OdysseusScriptException
	 */
	private void processUseThreadedBuffer(String useThreadedBuffer)
			throws OdysseusScriptException {
		// only true or false are allowed for parameter useThreadedBuffer
		if (!useThreadedBuffer.equalsIgnoreCase("true")
				&& !useThreadedBuffer.equalsIgnoreCase("false")) {
			throw new OdysseusScriptException(
					"Value for ThreadedBuffer is invalid. Valid values are true or false.");
		} else {
			this.useThreadedBuffer = Boolean.parseBoolean(useThreadedBuffer);
		}
	}

	/**
	 * processes and validates the value for opimization parameter
	 * 
	 * @param optimization
	 * @throws OdysseusScriptException
	 */
	private void processOptimizationValue(String optimization)
			throws OdysseusScriptException {
		// only true or false are allowed for parameter optimization
		if (!optimization.equalsIgnoreCase("true")
				&& !optimization.equalsIgnoreCase("false")) {
			throw new OdysseusScriptException(
					"Value for alowOptimization is invalid. Valid values are true or false.");
		} else {
			allowOptimization = Boolean.parseBoolean(optimization);
		}
	}

	/**
	 * adds the pretransformationhandlerParameter to build settings. also checks
	 * if this transformation handler exists only once
	 * 
	 * @param settings
	 * @throws OdysseusScriptException
	 */
	private void addPreTransformationHandlerParameter(
			List<IQueryBuildSetting<?>> settings)
			throws OdysseusScriptException {
		// if handler exists, create parameter
		PreTransformationHandlerParameter newHandlerParameter = createPreTransformationHandlerParameter(
				globalDegreeOfParallelization, globalBufferSize,
				allowOptimization, useThreadedBuffer);

		// check if another transformation handler already exists
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
						// parallelization keyword definition is only once
						// allowed
						if (newPair.name.equalsIgnoreCase(existingPair.name)) {
							throw new OdysseusScriptException(
									"Duplicate definition for "
											+ ParallelizationPreParserKeyword.KEYWORD
											+ " keyword is not allowed");
						}
					}
					// if another parameter exists, add this one
					existingHandlerParameter.add(newPair.name,
							newPair.parameters);
				}
				parameterAlreadyAdded = true;
				break;
			}
		}

		// if no transformation handler already exists, create a new one
		if (!parameterAlreadyAdded) {
			settings.add(newHandlerParameter);
		}
	}

	/**
	 * creates the pretRansformationhandler out of the given parameters. This
	 * values are needed in execution of preTransformationHandler
	 * 
	 * @param globalDegreeOfParallelization
	 * @param globalBufferSize
	 * @param allowCleanup
	 * @param useThreadedBuffer
	 * @return
	 */
	private PreTransformationHandlerParameter createPreTransformationHandlerParameter(
			int globalDegreeOfParallelization, int globalBufferSize,
			boolean allowCleanup, boolean useThreadedBuffer) {
		PreTransformationHandlerParameter preTransformationHandlerParameter = new PreTransformationHandlerParameter();

		// create parameter for global value (degree of parallelization)
		List<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		Pair<String, String> degreeParameter = new Pair<String, String>();
		degreeParameter
				.setE1(InterOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION
						.name());
		degreeParameter.setE2(String.valueOf(globalDegreeOfParallelization));
		parameters.add(degreeParameter);

		// create parameter for global value (buffersize)
		Pair<String, String> buffersizeParameter = new Pair<String, String>();
		buffersizeParameter
				.setE1(InterOperatorGlobalKeywordParameter.BUFFERSIZE.name());
		buffersizeParameter.setE2(String.valueOf(globalBufferSize));
		parameters.add(buffersizeParameter);

		// create parameter for global value (allow cleanup)
		Pair<String, String> optimizationParameter = new Pair<String, String>();
		optimizationParameter
				.setE1(InterOperatorGlobalKeywordParameter.OPTIMIZATION.name());
		optimizationParameter.setE2(String.valueOf(allowCleanup));
		parameters.add(optimizationParameter);

		// create parameter for global value (use threaded buffer)
		Pair<String, String> threadedBufferParameter = new Pair<String, String>();
		threadedBufferParameter
				.setE1(InterOperatorGlobalKeywordParameter.THREADEDBUFFER
						.name());
		threadedBufferParameter.setE2(String.valueOf(useThreadedBuffer));
		parameters.add(threadedBufferParameter);

		preTransformationHandlerParameter
				.add(InterOperatorParallelizationPreTransformationHandler.HANDLER_NAME,
						parameters);

		return preTransformationHandlerParameter;
	}

	/**
	 * validates and processes the degree of parallelization, also the constant
	 * AUTO is possible. If this constant is used the number of cores is
	 * detected and used as this value
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
							"AUTO detection of parallelization degree is not possible, because there is only one core detected.");
				}
				this.globalDegreeOfParallelization = availableCores;
			}
		}
	}

	/**
	 * validates and processes the value for buffersize. if the constant AUTO is
	 * set, the default buffersize is used
	 * 
	 * @param buffersizeString
	 * @throws OdysseusScriptException
	 */
	private void processBuffersizeParameter(String buffersizeString)
			throws OdysseusScriptException {
		try {
			globalBufferSize = Integer.parseInt(buffersizeString);
			if (globalBufferSize < 1) {
				throw new OdysseusScriptException();
			}
		} catch (NumberFormatException e) {
			if (buffersizeString.equalsIgnoreCase("AUTO")) {
				globalBufferSize = InterOperatorParallelizationConstants.DEFAULT_BUFFER_SIZE;
			} else {
				throw new OdysseusScriptException();
			}
		}
	}
}
