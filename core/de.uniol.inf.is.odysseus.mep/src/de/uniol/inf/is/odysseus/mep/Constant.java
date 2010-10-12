package de.uniol.inf.is.odysseus.mep;

import java.util.Collections;
import java.util.Set;

public class Constant implements IExpression {

	private final Object value;

	public Constant(Object value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
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
}
