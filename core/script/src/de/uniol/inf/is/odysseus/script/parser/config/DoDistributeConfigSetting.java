package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptConfigSetting;

public class DoDistributeConfigSetting implements IOdysseusScriptConfigSetting {

	private static final Collection<String> ALLOWED_PARAMETERS = Lists.newArrayList("TRUE", "FALSE");
	
	@Override
	public String getName() {
		return "Distribute";
	}

	@Override
	public boolean isValidValue(String value, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		return ALLOWED_PARAMETERS.contains(value.toUpperCase());
	}

	@Override
	public void set(String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		if ("TRUE".equals(newValue.toUpperCase())) {
			queryBuildSettings.add(ParameterDoDistribute.TRUE);
		} else {
			queryBuildSettings.add(ParameterDoDistribute.FALSE);
		}
	}

	@Override
	public Collection<String> getAllowedValues(ISession caller) {
		return ALLOWED_PARAMETERS;
	}

}
