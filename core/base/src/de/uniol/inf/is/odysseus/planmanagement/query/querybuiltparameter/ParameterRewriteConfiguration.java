package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;

public class ParameterRewriteConfiguration extends Setting<RewriteConfiguration> implements IQueryBuildSetting<RewriteConfiguration> {

	public ParameterRewriteConfiguration(RewriteConfiguration value) {
		super(value);
	}

}
