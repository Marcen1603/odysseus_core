package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;

public class NamedExpression implements IClone {

	public final String name;
	public final SDFExpression expression;
	public final SDFDatatype datatype;

	public NamedExpression(String name, SDFExpression expression, SDFDatatype datatype) {
		this.name = name;
		this.expression = expression;
		this.datatype = datatype;
	}

	public NamedExpression(String name, SDFExpression expression) {
		this.name = name;
		this.expression = expression;
		this.datatype = expression.getType();
	}

	public NamedExpression(NamedExpression namedExpression) {
		this.name = namedExpression.name;
		this.expression = namedExpression.expression.clone();
		if (namedExpression.datatype != null) {
			this.datatype = namedExpression.datatype;
		} else {
			this.datatype = null;
		}
	}

	public NamedExpression(String newName, NamedExpression copyFrom) {
		this.name = newName;
		this.expression = copyFrom.expression.clone();
		this.datatype = copyFrom.datatype;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("['");
		str.append(expression);
		str.append("','");
		str.append(name);
		if (datatype != null) {
			str.append(("','"));
			str.append(datatype);
		}
		str.append("']'");
		return str.toString();
	}

	@Override
	public NamedExpression clone() {
		return new NamedExpression(this);
	}
}
