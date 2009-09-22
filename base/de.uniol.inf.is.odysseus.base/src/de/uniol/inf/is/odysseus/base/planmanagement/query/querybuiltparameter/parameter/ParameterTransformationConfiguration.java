package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;

/**
 * {@link AbstractQueryBuildParameter} which provides a a
 * {@link TransformationConfiguration} for the query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterTransformationConfiguration extends
		AbstractQueryBuildParameter<TransformationConfiguration> {

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
