package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class ParameterQueryName extends Setting<Resource> implements IQueryBuildSetting<Resource> {

	private static final long serialVersionUID = 6807349033596261427L;

	public ParameterQueryName(Resource value) {
		super(value);
	}

}
