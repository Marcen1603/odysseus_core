package de.uniol.inf.is.odysseus.parallelization.intraoperator.preexecution;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.parallelization.autodetect.PerformanceDetectionHelper;
import de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter.IntraOperatorGlobalKeywordParameter;
import de.uniol.inf.is.odysseus.parallelization.preexecution.AbstractParallelizationPreExecutionHandler;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.parameter.IKeywordParameter;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class IntraOperatorPreExecutionHandler extends
		AbstractParallelizationPreExecutionHandler {

	public static final String TYPE = "INTRA_OPERATOR";
	private PreParserKeywordParameterHelper<IntraOperatorGlobalKeywordParameter> parameterHelper;
	private static final InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(IntraOperatorPreExecutionHandler.class);

	private int globalDegreeOfParallelization = 0;

	public IntraOperatorPreExecutionHandler() {
		parameterHelper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorGlobalKeywordParameter.class);
	}

	@Override
	public void validateParameters(String parameterString) {
		parameterHelper.validateParameterString(parameterString);
	}

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
		
	}

	@Override
	public String getType() {
		return TYPE;
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
}
