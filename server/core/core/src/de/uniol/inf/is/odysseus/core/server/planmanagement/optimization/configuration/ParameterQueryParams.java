package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterQueryParams extends Setting<Map<String, String>> implements IQueryBuildSetting<Map<String, String>> {

	private static final long serialVersionUID = 6807349033596261427L;

	public ParameterQueryParams(Map<String, String> value) {
		super(value);
	}

}
