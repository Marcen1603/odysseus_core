package de.uniol.inf.is.odysseus.multithreaded.planmanagement;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.IOptimizationSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterInterOperatorParallelization extends Setting<Integer>
		implements IOptimizationSetting<Integer>, IQueryBuildSetting<Integer> {

	public ParameterInterOperatorParallelization(Integer value) {
		super(value);
	}

}
