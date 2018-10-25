package de.uniol.inf.is.odysseus.script.generator.impl;

import java.util.List;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGenerator;

public class OdysseusScriptGenerator implements IOdysseusScriptGenerator {

	private static IPQLGenerator pqlGenerator;
	
	// called by OSGi
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = serv;
		}
	}

	@Override
	public String generate(ILogicalOperator rootOfQuery, QueryBuildConfiguration config) {
		Objects.requireNonNull(rootOfQuery, "RootOfQuery must not be null!");
		Objects.requireNonNull(config, "config must not be null!");
		
		StringBuilder sb = new StringBuilder();
	
		sb.append("#PARSER PQL\n");
		sb.append("#ADDQUERY\n");
		sb.append(pqlGenerator.generatePQLStatement(rootOfQuery));

		return sb.toString();
	}
	
	@Override
	public String generate(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		Objects.requireNonNull(queries, "List of queries to generate odysseus script must not be null!");
		// Preconditions.checkArgument(!queries.isEmpty(), "List of queries to generate odysseus script must not be empty!");
		Objects.requireNonNull(config, "QueryBuildConfiguration must not be null!");

		StringBuilder sb = new StringBuilder();

		sb.append("#PARSER PQL\n");

		for (ILogicalQuery query : queries) {
			sb.append("#ADDQUERY\n");
			sb.append(pqlGenerator.generatePQLStatement(query.getLogicalPlan()));
			sb.append("\n\n");
		}

		return sb.toString();
	}
	
	@Override
	public String wrap(List<String> pqlStatements, QueryBuildConfiguration config) {
		Objects.requireNonNull(pqlStatements, "List of pqlStatements must not be null!");
		// Preconditions.checkArgument(!pqlStatements.isEmpty(), "List of pqlStatements must not be empty!");
		Objects.requireNonNull(config, "Config must not be null!");
		
		StringBuilder sb = new StringBuilder();
		sb.append("#PARSER PQL\n");

		for (String statement: pqlStatements) {
			sb.append("#ADDQUERY\n");
			sb.append(statement);
			sb.append("\n");
		}

		return sb.toString();
	}
}
