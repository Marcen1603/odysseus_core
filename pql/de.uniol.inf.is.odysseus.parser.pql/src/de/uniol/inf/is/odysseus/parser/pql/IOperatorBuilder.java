package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

public interface IOperatorBuilder {
	public ILogicalOperator createOperator(Map<String, Object> parameters, List<ILogicalOperator> inputOps);
}
