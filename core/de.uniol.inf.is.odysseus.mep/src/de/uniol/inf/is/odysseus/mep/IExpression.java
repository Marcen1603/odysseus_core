package de.uniol.inf.is.odysseus.mep;

import java.util.Set;

public interface IExpression<T> {
	public T getValue();
	public Object acceptVisitor(IExpressionVisitor visitor, Object data);
	public Set<Variable> getVariables();
	public Class<? extends T> getType();
}
