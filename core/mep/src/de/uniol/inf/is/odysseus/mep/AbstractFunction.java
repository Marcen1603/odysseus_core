package de.uniol.inf.is.odysseus.mep;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractFunction<T> implements IFunction<T> {

	private IExpression<?>[] arguments;

	@Override
	final public void setArguments(IExpression<?>... arguments) {
		if (arguments.length != getArity()) {
			throw new IllegalArgumentException(
					"illegal number of arguments for function " + getSymbol());
		}
		this.arguments = arguments;
	}
	
	@Override
	public void setArgument(int argumentPosition, IExpression<?> argument) {
		this.arguments[argumentPosition] = argument;
	}

	@Override
	public IExpression<?>[] getArguments() {
		return arguments;
	}

	@SuppressWarnings("unchecked")
	final protected <S> S getInputValue(int argumentPos) {
		return (S) arguments[argumentPos].getValue();
	}

	final protected Double getNumericalInputValue(int argumentPos) {
		return ((Number) arguments[argumentPos].getValue()).doubleValue();
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		for (int i = 0; i < getArity(); ++i) {
			variables.addAll(getArguments()[i].getVariables());
		}
		return variables;
	}
	
	@Override
	public Variable getVariable(String name) {
		Set<Variable> variables = getVariables();
		for(Variable curVar : variables){
			if (curVar.getIdentifier().equals(name)) {
				return curVar;
			}
		}
		return null;
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getSymbol());
		builder.append('(');
		if (getArity() > 0) {
			builder.append(getArguments()[0].toString());
			for (int i = 1; i < getArity(); ++i) {
				builder.append(", ");
				builder.append(getArguments()[i].toString());
			}
		}
		builder.append(')');
		return builder.toString();
	}

	public boolean isContextDependent() {
		return false;
	}
	
	@Override
	public IExpression<?> getArgument(int argumentPosition) {
		return this.arguments[argumentPosition];
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert IFunction to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		return this;
	}

	@Override
	public Constant<T> toConstant() {
		throw new RuntimeException("cannot convert IFunction to Constant");
	}
}
