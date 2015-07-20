package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParallelIntraOperatorSetting extends Setting<ParallelIntraOperatorSettingValue>implements IQueryBuildSetting<ParallelIntraOperatorSettingValue> {
	
	public ParallelIntraOperatorSetting(ParallelIntraOperatorSettingValue value) {
		super(value);
	}

	public ParallelIntraOperatorSetting() {
		super(new ParallelIntraOperatorSettingValue());
	}
}
