package de.uniol.inf.is.odysseus.rcp.viewer.objectfusionconfig;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.transformation.helper.broker.BrokerTransformationHelper;


public class ObjectFusionQueryBuildConfiguration implements IQueryBuildConfiguration {
	
	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();
	
	public ObjectFusionQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						new BrokerTransformationHelper(),
						"relational",
						ITimeInterval.class.getName(),
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey",
						"de.uniol.inf.is.odysseus.latency.ILatency",
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability", 
						"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval",
						"de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime")));
	}

	@Override
	public List<IQueryBuildSetting<?>> get() {
		return settings;
	}
}
