package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

public class Constant<T> implements IExpression<T> {

	private final T value;

	public Constant(T value) {
		this.value = value;
	}
	
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Variable> getVariables() {
		return Collections.EMPTY_SET;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) value.getClass();
	}

	@Override
	public Variable getVariable(String name) {
		return null;
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert Constant to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		throw new RuntimeException("cannot convert Constant to IFunction");
	}

	@Override
	public Constant<T> toConstant() {
		return this;
	}
}
