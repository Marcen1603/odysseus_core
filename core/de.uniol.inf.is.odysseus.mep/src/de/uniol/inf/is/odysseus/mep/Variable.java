package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

public class Variable implements IExpression {
	private Object value;
	private final String identifier;

	public Variable(String id) {
		this.identifier = id;
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
}
