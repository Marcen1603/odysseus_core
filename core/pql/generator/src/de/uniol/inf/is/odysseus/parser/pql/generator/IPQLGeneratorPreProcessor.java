package de.uniol.inf.is.odysseus.parser.pql.generator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public interface IPQLGeneratorPreProcessor {

	void preprocess( ILogicalOperator logicalPlan );
	
}
