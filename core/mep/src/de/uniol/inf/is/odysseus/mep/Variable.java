package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

public class Variable implements IExpression<Object> {
	private Object value;
	private final String identifier;
	private final Class<?> type;
	
	public Variable(String id) {
		this.identifier = id;
		this.type = Object.class;
	}
	
	public Variable(String id, Class<?> type) {
		this.identifier = id;
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void bind(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}

	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return identifier + "[ " + value + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		return ((Variable) obj).identifier.equals(this.identifier);
	}
	
	@Override
	public int hashCode() {
		return this.identifier.hashCode();
	}
	
	@Override
	public Set<Variable> getVariables() {
		return Collections.singleton(this);
	}
	
	@Override
	public Variable getVariable(String varName) {
		return this.identifier.equals(varName) ? this : null;
	}
	
	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		return this;
	}

	@Override
	public IFunction<Object> toFunction() {
		throw new RuntimeException("cannot convert Variable to IFunction");
	}

	@Override
	public Constant<Object> toConstant() {
		throw new RuntimeException("cannot convert Variable to Constant");
	}
}
