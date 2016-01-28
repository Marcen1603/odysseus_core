package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;

public class NamedExpression {

	public final String name;
	public final SDFExpression expression;
	public final SDFDatatype datatype;
	
	public NamedExpression(String name, SDFExpression expression, SDFDatatype datatype) {
		this.name = name;
		this.expression = expression;
		this.datatype = datatype;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("['");
		str.append(expression);
		str.append("','");
		str.append(name);
		if (datatype != null){
			str.append(("','"));
			str.append(datatype);
		}
		str.append("']'");
		return str.toString();
	}
			
}	
