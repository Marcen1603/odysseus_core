package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterFragmentationType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.executor.ExecutorHandler;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * This class represents the keyword for the data fragmentation strategies. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #DATAFRAGMENTATIONTYPE name source additionalParameters; name source additionalParameters; ...
 * @author Michael Brand
 */
public class FragmentationTypePreParserKeyword extends AbstractPreParserKeyword {

	/**
	 * The string representation of the keyword.
	 */
	public static final String KEYWORD = "DATAFRAGMENTATIONTYPE";
	
	/**
	 * The separator between different fragmentation strategies for different sources
	 */
	public static final String OUTER_SEP = ";";
	
	/**
	 * The separator between fragmentation strategy, source name and other parameters for the fragmentation strategy
	 */
	public static final String INNER_SEP = " ";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		
		String[] strategies = parameter.split(FragmentationTypePreParserKeyword.OUTER_SEP);
		if(strategies == null || strategies.length == 0)
			throw new OdysseusScriptException("No data fragmentation strategy defined.");
		
		for(String strategy : strategies) {
			
			String[] parameters = strategy.split(INNER_SEP);
			if(parameters == null || parameters.length == 0)
				throw new OdysseusScriptException("At least one data fragmentation strategy is not defined.");
			
			String strategyName = parameters[0];
			if(strategyName == null || !ExecutorHandler.getServerExecutor().getDataFragmentationNames().contains(strategyName))
				throw new OdysseusScriptException("Specified data fragmentation strategy '" + strategyName + 
						"' not found.");
			
		}
		
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		
		if(!Strings.isNullOrEmpty(parameter))
			addSettings.add(new ParameterFragmentationType(parameter));
		
		return null;
	}

}