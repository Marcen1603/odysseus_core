package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;

/**
 * @author Jonas Jacobi
 */
public interface IOperatorBuilder {
	public Set<Parameter<?>> getParameters();

	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps);
}
