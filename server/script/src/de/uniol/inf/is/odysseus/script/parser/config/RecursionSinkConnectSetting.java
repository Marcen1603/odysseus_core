package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterRecursionConnectSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class RecursionSinkConnectSetting extends AbstractBooleanScriptConfigSetting {
	
	static final String NAME = "RecursionSinkConnect";
	
	@Override
	public void set(String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings,
			ISession caller) {
		queryBuildSettings.add("TRUE".equals(newValue.toUpperCase())?ParameterRecursionConnectSink.TRUE: ParameterRecursionConnectSink.FALSE);
	}
		
	@Override
	public String getName() {
		return NAME;
	}

}
