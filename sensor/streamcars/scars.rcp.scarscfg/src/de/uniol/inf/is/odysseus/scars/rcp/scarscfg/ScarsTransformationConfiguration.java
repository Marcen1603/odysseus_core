package de.uniol.inf.is.odysseus.scars.rcp.scarscfg;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IParameterTransformationConfigurationExtension;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;


public class ScarsTransformationConfiguration implements IParameterTransformationConfigurationExtension {

	private static ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(
			new BrokerTransformationHelper(),
			"relational",
			
			// ObjectTracking
			"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval", // ok
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey", // ok
			"de.uniol.inf.is.odysseus.latency.ILatency",  // ok
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability",  // ok
			"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime", // ok
			
			// StreamCars
			"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer", // ok
			"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain")); // ok
		
	@Override
	public ParameterTransformationConfiguration get() {
		trafoConfigParam.getValue().setOption("IBrokerInterval", true);
		return trafoConfigParam;
	}

}
