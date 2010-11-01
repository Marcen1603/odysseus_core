package de.uniol.inf.is.odysseus.scars.rcp.scarscfg;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;

public class ScarsQueryBuildConfiguration implements IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();
	
	public ScarsQueryBuildConfiguration() {
		TransformationConfiguration cfg = new TransformationConfiguration(
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
				"de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain"); // ok
				
		
		cfg.setOption("IBrokerInterval", true);
		settings.add(new ParameterTransformationConfiguration(cfg));
				
	}

	@Override
	public List<IQueryBuildSetting<?>> get() {
		return settings;
	}
		
//	@Override
//	public ParameterTransformationConfiguration get() {
//		trafoConfigParam.getValue().setOption("IBrokerInterval", true);
//		return trafoConfigParam;
//	}

}
