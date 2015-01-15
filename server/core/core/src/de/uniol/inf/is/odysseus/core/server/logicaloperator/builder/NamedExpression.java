package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;

public class NamedExpression {

	public final String name;
	public final SDFExpression expression;
	
	public NamedExpression(String name, SDFExpression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return "['"+expression+"','"+name+"']";
	}
	
		
}	
