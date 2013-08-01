package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterPerformQuerySharing;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptConfigSetting;

public class QuerySharingConfigSetting implements IOdysseusScriptConfigSetting {

	@Override
	public String getName() {
		return "QuerySharing";
	}

	@Override
	public boolean isValidValue(String value, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		try {
			Boolean.valueOf(value);
			return true;
		} catch( Throwable t ) {
			return false;
		}
	}

	@Override
	public void set(String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		if ("TRUE".equals(newValue.toUpperCase())) {
			queryBuildSettings.add(ParameterPerformQuerySharing.TRUE);
		} else {
			queryBuildSettings.add(ParameterPerformQuerySharing.FALSE);
		}
	}
	
	@Override
	public Collection<String> getAllowedValues(ISession caller) {
		return Lists.newArrayList("TRUE", "FALSE");
	}

}
