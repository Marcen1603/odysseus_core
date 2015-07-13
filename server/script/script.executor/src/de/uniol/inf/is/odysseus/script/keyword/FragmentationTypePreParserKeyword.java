package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserExecutorKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * This class represents the keyword for the data fragmentation strategies. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #DATAFRAGMENTATIONTYPE name source additionalParameters; name source additionalParameters; ...
 * @author Michael Brand
 */
public class FragmentationTypePreParserKeyword extends AbstractPreParserExecutorKeyword {

	/**
	 * The string representation of the keyword.
	 */
	public static final String KEYWORD = "DATAFRAGMENTATIONTYPE";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		
		String[] strategies = parameter.split(ParameterFragmentationType.OUTER_SEP);
		if(strategies == null || strategies.length == 0)
			throw new OdysseusScriptException("No data fragmentation strategy defined.");
		
		for(String strategy : strategies) {
			
			String[] parameters = strategy.split(ParameterFragmentationType.INNER_SEP);
			if(parameters == null || parameters.length == 0)
				throw new OdysseusScriptException("At least one data fragmentation strategy is not defined.");
			
			String strategyName = parameters[0];
			if(strategyName == null || !getServerExecutor().getDataFragmentationNames().contains(strategyName))
				throw new OdysseusScriptException("Specified data fragmentation strategy '" + strategyName + 
						"' not found.");
			
		}
		
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		
		if(!Strings.isNullOrEmpty(parameter))
			addSettings.add(new ParameterFragmentationType(parameter));
		
		return null;
	}

}