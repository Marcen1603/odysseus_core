package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterQueryName extends Setting<String> implements IQueryBuildSetting<String>, Serializable {

	private static final long serialVersionUID = 6807349033596261427L;

	public ParameterQueryName(String value) {
		super(value);
	}

}
