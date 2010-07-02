package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IParameterTransformationConfigurationExtension;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;

public class DefaultParameterTransformationConfiguration implements IParameterTransformationConfigurationExtension {

	@SuppressWarnings("unchecked")
	private static ParameterTransformationConfiguration trafoConfigParam = 
			new ParameterTransformationConfiguration(
					new TransformationConfiguration(
							new RelationalTransformationHelper(), 
							"relational", 
							ITimeInterval.class));

	@Override
	public ParameterTransformationConfiguration get() {
		return trafoConfigParam;
	}

}
