package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.AbstractQueryBuildSetting;

public interface IQueryBuildConfiguration {

	public List<AbstractQueryBuildSetting<?>> get();
}
