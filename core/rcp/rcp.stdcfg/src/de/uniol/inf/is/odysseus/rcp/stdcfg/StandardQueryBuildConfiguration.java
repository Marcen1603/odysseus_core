package de.uniol.inf.is.odysseus.rcp.stdcfg;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterInstallMetadataListener;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryBuildConfiguration;

public class StandardQueryBuildConfiguration implements
		IQueryBuildConfiguration {

	private List<IQueryBuildSetting> settings = new ArrayList<IQueryBuildSetting>();
	
	@SuppressWarnings("unchecked")
	public StandardQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						"relational", 
						ITimeInterval.class)));
		//settings.add(new ParameterBufferPlacementStrategy("Standard Buffer Placement"));
		settings.add(new ParameterBufferPlacementStrategy("Source Buffer Placement"));
		settings.add(ParameterInstallMetadataListener.TRUE);
	}

	@Override
	public List<IQueryBuildSetting> get() {
		return settings;
	}

}
