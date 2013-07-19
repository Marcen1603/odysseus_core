package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDataFragmentation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

/**
 * This class represents the keyword for the data fragmentation. <br />
 * In Odysseus Script it can be used as follows: <br />
 * #DODATAFRAGMENTATION true/false
 * @author Michael Brand
 */
public class UseFragmentationPreParserKeyword extends AbstractPreParserKeyword {

	/**
	 * The string representation of the keyword.
	 */
	public static final String DODATAFRAGMENTATION = "DODATAFRAGMENTATION";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {

	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter,
			ISession caller) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		
		if("TRUE".equals(parameter.toUpperCase()))
			addSettings.add(ParameterDoDataFragmentation.TRUE);
		else addSettings.add(ParameterDoDataFragmentation.FALSE);
		
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		
		return Arrays.asList("true", "false");
		
	}

}