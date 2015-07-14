package de.uniol.inf.is.odysseus.recovery.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

// TODO javaDoc
public class RecoveryExecutorConfigSettingParameter extends Setting<String>
		implements IQueryBuildSetting<String> {
	
	public static RecoveryExecutorConfigSettingParameter getDefault() {
		return new RecoveryExecutorConfigSettingParameter("None");
	}

	public RecoveryExecutorConfigSettingParameter(String value) {
		super(value);
	}

}