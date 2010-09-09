package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter;

public class ParameterInstallMetadataListener extends
		AbstractQueryBuildParameter<Boolean> {

	protected ParameterInstallMetadataListener(Boolean value) {
		super(value);
	}
	
	public static ParameterInstallMetadataListener TRUE = new ParameterInstallMetadataListener(true);
	public static ParameterInstallMetadataListener FALSE = new ParameterInstallMetadataListener(false);
}
