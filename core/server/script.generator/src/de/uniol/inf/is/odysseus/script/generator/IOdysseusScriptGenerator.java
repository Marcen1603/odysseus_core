package de.uniol.inf.is.odysseus.script.generator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IOdysseusScriptGenerator {

	public String generate( List<ILogicalQuery> queries, QueryBuildConfiguration config );
	public String generate( ILogicalOperator rootOfQuery, QueryBuildConfiguration config );
	
	public String wrap( List<String> pqlStatements, QueryBuildConfiguration config );
	
}
