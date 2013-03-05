package de.uniol.inf.is.odysseus.parser.pql.generator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IPQLGenerator {

	public String generatePQLStatement(ILogicalOperator startOperator);

}
