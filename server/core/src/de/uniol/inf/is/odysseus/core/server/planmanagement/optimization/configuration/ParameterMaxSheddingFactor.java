package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterMaxSheddingFactor extends Setting<Integer> implements IQueryBuildSetting<Integer>, Serializable {

	private static final long serialVersionUID = 5085574251441271192L;
	
	public ParameterMaxSheddingFactor() {
		super(null);
	}

	public ParameterMaxSheddingFactor(Integer value) {
		super(value);
	}

}
