package de.uniol.inf.is.odysseus.mep;

import java.util.Set;

public interface IExpression<T> {
	public T getValue();
	public Object acceptVisitor(IExpressionVisitor visitor, Object data);
	public Set<Variable> getVariables();
	public Variable getVariable(String name);
	public Class<? extends T> getType();
	public boolean isVariable();
	public boolean isFunction();
	public boolean isConstant();
	public Variable toVariable();
	public IFunction<T> toFunction();
	public Constant<T> toConstant();
}
