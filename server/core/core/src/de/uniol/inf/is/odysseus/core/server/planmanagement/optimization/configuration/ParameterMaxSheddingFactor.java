package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterMaxSheddingFactor extends Setting<Integer> implements IQueryBuildSetting<Integer>{

	public ParameterMaxSheddingFactor(Integer value) {
		super(value);
	}

}
