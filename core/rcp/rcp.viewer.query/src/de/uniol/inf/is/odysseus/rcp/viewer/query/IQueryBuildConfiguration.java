package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public interface IQueryBuildConfiguration {

	public List<IQueryBuildSetting> get();
}
