package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class QueryPartitionerParameter extends Setting<String> implements IQueryBuildSetting<String> {

	public QueryPartitionerParameter(String value) {
		super(value);
	}

}
