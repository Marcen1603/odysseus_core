package de.uniol.inf.is.odysseus.core.server.planmanagement.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

abstract public class AbstractQueryBuildConfiguration implements
		IQueryBuildConfiguration {

	final protected List<IQueryBuildSetting<?>> settings = new ArrayList<IQueryBuildSetting<?>>();

	@Override
	final public List<IQueryBuildSetting<?>> getConfiguration() {
		return Collections.unmodifiableList(settings);
	}
	
	@Override
	public abstract IQueryBuildConfiguration clone();

}
