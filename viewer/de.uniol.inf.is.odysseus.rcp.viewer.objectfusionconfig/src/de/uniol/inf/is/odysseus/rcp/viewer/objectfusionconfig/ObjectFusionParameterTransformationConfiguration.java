package de.uniol.inf.is.odysseus.rcp.viewer.objectfusionconfig;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IParameterTransformationConfigurationExtension;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;


public class ObjectFusionParameterTransformationConfiguration implements IParameterTransformationConfigurationExtension {
	
	private static ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(
			new BrokerTransformationHelper(),
			"relational",
			ITimeInterval.class.getName(),
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey",
			"de.uniol.inf.is.odysseus.latency.ILatency",
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability", 
			"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval",
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime"));

	@Override
	public ParameterTransformationConfiguration get() {
		return trafoConfigParam;
	}

}
