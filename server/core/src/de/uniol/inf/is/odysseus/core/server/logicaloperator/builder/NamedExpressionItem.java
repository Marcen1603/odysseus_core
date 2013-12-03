package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;

public class NamedExpressionItem {

	public final String name;
	public final SDFExpression expression;
	
	public NamedExpressionItem(String name, SDFExpression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		return "['"+expression+"','"+name+"']";
	}
	
		
}	
