package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserVisitor;

public interface IDatabaseAOVisitor extends NewSQLParserVisitor {

	public String getAlias();
	
}
