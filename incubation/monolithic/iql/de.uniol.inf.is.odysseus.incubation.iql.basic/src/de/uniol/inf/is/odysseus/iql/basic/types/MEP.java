package de.uniol.inf.is.odysseus.iql.basic.types;

import de.uniol.inf.is.odysseus.core.mep.IExpression;


public class MEP {
	
	public static Object get(String expr) {
		IExpression<?> expression = de.uniol.inf.is.odysseus.mep.MEP.getInstance().parse(expr);
		return expression.getValue();
	}

}
