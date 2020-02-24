package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DoDistributeConfigSetting extends AbstractBooleanScriptConfigSetting {
	
	public static final String NAME = "Distribute";
	
	@Override
	public void set(String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		queryBuildSettings.add("TRUE".equals(newValue.toUpperCase())?ParameterDoDistribute.TRUE:ParameterDoDistribute.FALSE);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
