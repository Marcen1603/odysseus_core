package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IPQLStatementGenerator<T extends ILogicalOperator> {

	public Class<T> getOperatorClass();
	public String generateStatement( T operator, Map<T, String> otherOperatorNames );
		
}
