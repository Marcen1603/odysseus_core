package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * {@link IQueryBuildSetting} which provides a a
 * {@link TransformationConfiguration} for the query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterTransformationConfiguration extends
		Setting<TransformationConfiguration> implements IQueryBuildSetting<TransformationConfiguration> {

	/**
	 * Creates a ParameterTransformationConfiguration.
	 * 
	 * @param value
	 *            a {@link TransformationConfiguration} for the query.
	 */
	public ParameterTransformationConfiguration(
			TransformationConfiguration value) {
		super(value);
	}
}
