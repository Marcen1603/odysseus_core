package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

public class ParameterInstallMetadataListener extends Setting<Boolean> implements
		IQueryBuildSetting<Boolean> {

	protected ParameterInstallMetadataListener(Boolean value) {
		super(value);
	}
	
	public static ParameterInstallMetadataListener TRUE = new ParameterInstallMetadataListener(true);
	public static ParameterInstallMetadataListener FALSE = new ParameterInstallMetadataListener(false);
}
