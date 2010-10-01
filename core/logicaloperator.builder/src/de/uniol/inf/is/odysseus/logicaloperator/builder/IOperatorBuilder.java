package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * @author Jonas Jacobi
 */
public interface IOperatorBuilder {
	public Set<IParameter<?>> getParameters();

	public ILogicalOperator createOperator();

	public boolean validate();

	public List<Exception> getErrors();

	public void setInputOperator(int inputPort, ILogicalOperator operator,
			int outputPort);

	public int getMinInputOperatorCount();

	public int getMaxInputOperatorCount();
	
	public void setCaller(User caller);

}
