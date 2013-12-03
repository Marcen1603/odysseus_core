package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterQueryName extends Setting<String> implements IQueryBuildSetting<String>{

	public ParameterQueryName(String value) {
		super(value);
	}

}
