package de.uniol.inf.is.odysseus.stdcfg;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterShareSimilarOperators;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.ParameterPerformQuerySharing;

public class StandardQueryBuildConfiguration implements
		IQueryBuildConfiguration {

	private List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();
	
	@SuppressWarnings("unchecked")
	public StandardQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						"relational", 
						ITimeInterval.class)));
		settings.add(ParameterPerformQuerySharing.TRUE);
		settings.add(ParameterShareSimilarOperators.FALSE);
	}

	@Override
	public List<IQueryBuildSetting<?>> getConfiguration() {
		return settings;
	}
	
	@Override
	public String getName() {
		return "Standard";
	}

}
