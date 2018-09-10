package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class TrafoOptionPreParserKeyword extends AbstractPreParserKeyword {

	public static final String TRAFOOPTION = "TRAFOOPTION";
	
	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables, String parameter,
			ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		String[] params = getSimpleParameters(parameter);
		
		if (params.length != 2){
			throw new OdysseusScriptException("Must be a key value pair of options");

		}
		
		String option = params[0];
		String value = params[1];
		
		List<IQueryBuildSetting<?>> addSettings = getAdditionalTransformationSettings(variables);
		// Determine if ParameterTransformationConfiguration is already set
		for (IQueryBuildSetting<?> s : addSettings) {
			if (s instanceof ParameterTransformationConfiguration) {
				((ParameterTransformationConfiguration) s)
						.getValue().setOption(option, value);
				return null;
			}
		}

		// Not found, create new
		ParameterTransformationConfiguration p = new ParameterTransformationConfiguration(
				new TransformationConfiguration());
		addSettings.add(p);
		p.getValue().setOption(option, value);
		
		
		return null;
	}

}
