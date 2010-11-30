package de.uniol.inf.is.odysseus.planmanagement.configuration;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public interface IQueryBuildConfiguration {

	public List<IQueryBuildSetting<?>> getConfiguration();
	public String getName();
}
