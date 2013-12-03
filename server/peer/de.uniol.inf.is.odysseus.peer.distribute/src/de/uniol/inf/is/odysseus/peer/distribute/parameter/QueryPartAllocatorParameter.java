package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class QueryPartAllocatorParameter extends Setting<String> implements IQueryBuildSetting<String> {

	public QueryPartAllocatorParameter(String value) {
		super(value);
	}

}
