package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class QuerySharingConfigSetting extends AbstractBooleanScriptConfigSetting {

	public static String NAME = "QuerySharing";
	
	@Override
	public void set(String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		queryBuildSettings.add("TRUE".equals(newValue.toUpperCase())?ParameterPerformQuerySharing.TRUE:ParameterPerformQuerySharing.FALSE);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

}
