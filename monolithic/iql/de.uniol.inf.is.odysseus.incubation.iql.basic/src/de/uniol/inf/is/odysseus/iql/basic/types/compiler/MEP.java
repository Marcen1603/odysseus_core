package de.uniol.inf.is.odysseus.iql.basic.types.compiler;



import de.uniol.inf.is.odysseus.core.mep.IMepExpression;

public class MEP {

	public static Object get(String name, String ... args) {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append("(");
		for (int i = 0; i< args.length; i++) {
			if (i > 0) {
				builder.append(", ");
			}
			builder.append(args[i]);
		}
		builder.append(")");
		IMepExpression<?> expression = de.uniol.inf.is.odysseus.mep.MEP.getInstance().parse(builder.toString());
		return expression.getValue();
	}

}
