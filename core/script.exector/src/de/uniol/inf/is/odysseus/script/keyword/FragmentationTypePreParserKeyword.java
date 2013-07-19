package de.uniol.inf.is.odysseus.script.keyword;

import java.util.Collection;
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
 * #DATAFRAGMENTATIONTYPE <name>
 * @author Michael Brand
 */
public class FragmentationTypePreParserKeyword extends AbstractPreParserKeyword {

	/**
	 * The string representation of the keyword.
	 */
	public static final String KEYWORD = "DATAFRAGMENTATIONTYPE";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {
		
		String[] parameters = parameter.split(" ");
		String strategyName = parameters[0];
		
		if(!ExecutorHandler.getServerExecutor().getDataFragmentationNames().contains(strategyName))
			throw new OdysseusScriptException("Specified data fragmentation strategy '" + strategyName + 
					"' not found.");
		
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		
		if(!Strings.isNullOrEmpty(parameter))
			addSettings.add(new ParameterFragmentationType(parameter));
		
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		
		return ExecutorHandler.getServerExecutor().getDataFragmentationNames();		
		
	}

}