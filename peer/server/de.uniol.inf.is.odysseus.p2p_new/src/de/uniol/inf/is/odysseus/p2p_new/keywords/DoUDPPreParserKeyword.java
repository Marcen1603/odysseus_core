package de.uniol.inf.is.odysseus.p2p_new.keywords;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.parameter.UseUDPParameter;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class DoUDPPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "DOUDP";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {

		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		if ("TRUE".equals(parameter.toUpperCase())) {
			addSettings.add(UseUDPParameter.TRUE);
		} else {
			addSettings.add(UseUDPParameter.FALSE);
		}
		return null;
	}
	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return Arrays.asList("true", "false");				
	}

}