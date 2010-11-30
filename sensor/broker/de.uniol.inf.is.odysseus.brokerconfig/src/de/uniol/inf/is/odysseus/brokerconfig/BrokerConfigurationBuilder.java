package de.uniol.inf.is.odysseus.brokerconfig;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.rules.BrokerTransformationHelper;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;

public class BrokerConfigurationBuilder implements IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();

	public BrokerConfigurationBuilder() {
		TransformationConfiguration cfg = new TransformationConfiguration(new BrokerTransformationHelper(), "relational",

		// ObjectTracking

				"de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval", // ok
				
				"de.uniol.inf.is.odysseus.latency.ILatency"
				// ok
				);
		cfg.setOption("IBrokerInterval", true);
		settings.add(new ParameterTransformationConfiguration(cfg));

	}

	@Override
	public List<IQueryBuildSetting<?>> getConfiguration() {
		return settings;
	}
	
	@Override
	public String getName() {
		return "BrokerConfig";
	}

	// @Override
	// public ParameterTransformationConfiguration get() {
	// trafoConfigParam.getValue().setOption("IBrokerInterval", true);
	// return trafoConfigParam;
	// }

}
