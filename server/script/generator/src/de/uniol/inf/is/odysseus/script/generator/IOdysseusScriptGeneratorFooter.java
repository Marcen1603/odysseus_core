package de.uniol.inf.is.odysseus.script.generator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IOdysseusScriptGeneratorFooter {

	public String getFooter( List<ILogicalQuery> queries, QueryBuildConfiguration config );

}
