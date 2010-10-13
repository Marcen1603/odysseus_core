package de.uniol.inf.is.odysseus.rcp.stdcfg;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.AbstractQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.query.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;

public class StandardQueryBuildConfiguration implements
		IQueryBuildConfiguration {

	private List<AbstractQueryBuildSetting<?>> settings = new ArrayList<AbstractQueryBuildSetting<?>>();
	
	@SuppressWarnings("unchecked")
	public StandardQueryBuildConfiguration() {
		settings.add(new ParameterTransformationConfiguration(
				new TransformationConfiguration(
						new RelationalTransformationHelper(), 
						"relational", 
						ITimeInterval.class)));
	}

	@Override
	public List<AbstractQueryBuildSetting<?>> get() {
		return settings;
	}

}
