package de.uniol.inf.is.odysseus.parallelization.intraoperator.parameter;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.parallelization.intraoperator.preexecution.IntraOperatorPreExecutionHandler;
import de.uniol.inf.is.odysseus.parallelization.keyword.ParallelizationPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.parameter.PreParserKeywordParameterHelper;

public class IntraOperatorGlobalKeywordBuilder {

	private static String BLANK = " ";
	
	public static String buildParameterString(
			Integer degree) {
		StringBuilder builder = new StringBuilder();
		builder.append("#" + ParallelizationPreParserKeyword.KEYWORD + BLANK);
		PreParserKeywordParameterHelper<IntraOperatorGlobalKeywordParameter> helper = PreParserKeywordParameterHelper
				.newInstance(IntraOperatorGlobalKeywordParameter.class);
		Map<IntraOperatorGlobalKeywordParameter, String> parameters = new HashMap<IntraOperatorGlobalKeywordParameter, String>();
		parameters.put(
				IntraOperatorGlobalKeywordParameter.PARALLELIZATION_TYPE,
				IntraOperatorPreExecutionHandler.TYPE);
		parameters.put(
				IntraOperatorGlobalKeywordParameter.DEGREE_OF_PARALLELIZATION,
				String.valueOf(degree));
		builder.append(helper.createParameterString(parameters));

		return builder.toString();
	}
}
