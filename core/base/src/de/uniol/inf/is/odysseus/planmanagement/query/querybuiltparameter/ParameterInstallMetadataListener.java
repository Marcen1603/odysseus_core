package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

public class ParameterInstallMetadataListener extends
		AbstractQueryBuildSetting<Boolean> {

	protected ParameterInstallMetadataListener(Boolean value) {
		super(value);
	}
	
	public static ParameterInstallMetadataListener TRUE = new ParameterInstallMetadataListener(true);
	public static ParameterInstallMetadataListener FALSE = new ParameterInstallMetadataListener(false);
}
