package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class QueryPartModificatorParameter extends Setting<String> implements IQueryBuildSetting<String> {

	public QueryPartModificatorParameter(String value) {
		super(value);
	}

}
