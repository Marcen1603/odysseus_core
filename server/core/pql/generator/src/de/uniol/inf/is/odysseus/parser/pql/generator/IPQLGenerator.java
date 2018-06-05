package de.uniol.inf.is.odysseus.parser.pql.generator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;

public interface IPQLGenerator {

	public String generatePQLStatement(ILogicalOperator startOperator);
	public String generatePQLStatement(ILogicalPlan plan);


}
