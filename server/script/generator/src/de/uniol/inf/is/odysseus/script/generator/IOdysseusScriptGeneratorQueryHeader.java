package de.uniol.inf.is.odysseus.script.generator;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IOdysseusScriptGeneratorQueryHeader {

	public String getQueryHeader( ILogicalQuery query, QueryBuildConfiguration config );
	
}
