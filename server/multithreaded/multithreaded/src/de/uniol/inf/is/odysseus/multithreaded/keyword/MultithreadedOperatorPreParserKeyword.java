package de.uniol.inf.is.odysseus.multithreaded.keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.multithreaded.parameter.MultithreadedOperatorParameter;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class MultithreadedOperatorPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "MTOPERATORS";

	private static final Logger LOG = LoggerFactory
			.getLogger(MultithreadedOperatorPreParserKeyword.class);
	private static final int MIN_ATTRIBUTE_COUNT = 1;
	private static final String PATTERN = "<1..n Unique operator Ids>";


	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("Parameters for " + KEYWORD
					+ " are missing");
		}

		String[] splitted = parameter.trim().split(" ");

		if (splitted.length < MIN_ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " requires at least "
					+ MIN_ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		} else {
			
		}

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		List<IQueryBuildSetting<?>> settings = getAdditionalTransformationSettings(variables);

		String[] splitted = parameter.trim().split(" ");
		List<String> operatorIds = new ArrayList<String>(Arrays.asList(splitted));
		LOG.debug("Multithreading for operators with id: "+operatorIds.toString());
		
		boolean parameterAdded = false;
		
		for( IQueryBuildSetting<?> setting : settings ) {
			if( setting.getClass().equals(MultithreadedOperatorParameter.class)) {
				MultithreadedOperatorParameter mtOperatorParameter = (MultithreadedOperatorParameter) setting;
				mtOperatorParameter.addOperatorId(operatorIds);
				parameterAdded = true;
			}
		}
		
		if (!parameterAdded){
			settings.add(new MultithreadedOperatorParameter(operatorIds));
		}

		return null;
	}
}
