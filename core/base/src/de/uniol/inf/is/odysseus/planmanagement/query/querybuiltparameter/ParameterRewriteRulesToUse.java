package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import java.util.Set;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.IOptimizationSetting;

public class ParameterRewriteRulesToUse extends Setting<Set<String>> implements 
		IQueryBuildSetting<Set<String>>, IOptimizationSetting<Set<String>> {

	protected ParameterRewriteRulesToUse(Set<String> value) {
		super(value);
	}

}
