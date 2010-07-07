package de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter;

public class ParameterInstallMetadataListener extends
		AbstractOptimizationParameter {

	public ParameterInstallMetadataListener(boolean value) {
		super(value);
	}
	
	public static final ParameterInstallMetadataListener TRUE = new ParameterInstallMetadataListener(true);
	public static final ParameterInstallMetadataListener FALSE = new ParameterInstallMetadataListener(false);
	
}
