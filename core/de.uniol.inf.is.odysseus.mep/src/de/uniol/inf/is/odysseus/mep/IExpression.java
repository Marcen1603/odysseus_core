package de.uniol.inf.is.odysseus.mep;

import java.util.Set;

public interface IExpression {
	public Object getValue();
	public Object acceptVisitor(IExpressionVisitor visitor, Object data);
	public Set<Variable> getVariables();
}
