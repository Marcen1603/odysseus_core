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

public class IntraOperatorParallelizationPreParserKeyword extends
		AbstractPreParserKeyword {
	public static final String KEYWORD = "INTRAOPERATOR";

	private static final Logger LOG = LoggerFactory
			.getLogger(IntraOperatorParallelizationPreParserKeyword.class);

	private PreParserKeywordParameterHelper<IntraOperatorParallelizationKeywordParameter> parameterHelper;

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor)
			throws OdysseusScriptException {
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorParallelizationKeywordParameter.class);
		parameterHelper.validateParameterString(parameter);

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context,
			IServerExecutor executor) throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);
		ParallelIntraOperatorSetting intraOperatorSetting = getParallelizationKeywordIfExists(settings);

		Map<IKeywordParameter, String> result = parameterHelper
				.parse(parameter);
		List<String> operatorIds = parseOperatorIds(result
				.get(IntraOperatorParallelizationKeywordParameter.OPERATORID));
		int individualDegree = parseDegree(result
				.get(IntraOperatorParallelizationKeywordParameter.DEGREE_OF_PARALLELIZATION));
		int individualBuffersize = parseBuffersize(result
				.get(IntraOperatorParallelizationKeywordParameter.BUFFERSIZE));

		for (String operatorId : operatorIds) {
			ParallelIntraOperatorSettingValueElement individualSettings = new ParallelIntraOperatorSettingValueElement(
					individualDegree, individualBuffersize);
			intraOperatorSetting.getValue().addIndividualSettings(operatorId,
					individualSettings);
		}

		return null;
	}

	private int parseBuffersize(String string) throws OdysseusScriptException {
		if (string == null || string.equalsIgnoreCase("AUTO")) {
			return IntraOperatorParallelizationConstants.DEFAULT_BUFFERSIZE;
		} else {
			try {
				int buffersize = Integer.parseInt(string);
				if (buffersize > 0){
					return buffersize;					
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

	private int parseDegree(String degreeOfParallelizationString)
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
			throw new OdysseusScriptException(
					"Value for degreeOfParallelization is not valid. Degree is no integer.");
		}
	}

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

	private ParallelIntraOperatorSetting getParallelizationKeywordIfExists(
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
