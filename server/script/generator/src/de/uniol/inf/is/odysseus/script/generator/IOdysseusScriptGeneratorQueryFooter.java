package de.uniol.inf.is.odysseus.script.generator;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IOdysseusScriptGeneratorQueryFooter {

	public String getQueryFooter( ILogicalQuery query, QueryBuildConfiguration config );
	
}
