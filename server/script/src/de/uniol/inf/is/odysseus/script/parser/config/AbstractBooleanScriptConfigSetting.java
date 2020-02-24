package de.uniol.inf.is.odysseus.script.parser.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoDistribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptConfigSetting;

public abstract class AbstractBooleanScriptConfigSetting implements IOdysseusScriptConfigSetting {

	private static final Collection<String> ALLOWED_PARAMETERS = Lists.newArrayList("TRUE", "FALSE");

	
	
	@Override
	public boolean isValidValue(String value, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller) {
		return ALLOWED_PARAMETERS.contains(value.toUpperCase());
	}

	@Override
	public Collection<String> getAllowedValues(ISession caller) {
		return ALLOWED_PARAMETERS;
	}

}
