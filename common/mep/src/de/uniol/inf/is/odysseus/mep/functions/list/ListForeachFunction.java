package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

public class ListForeachFunction extends AbstractLambdaListFunction {

	private static final long serialVersionUID = -8269748747594828192L;
	
	public ListForeachFunction() {
		super("foreach");
	}

	@Override
	protected void fillReturnList(List<Object> out, Object o) {
		out.add(expression.getValue());	
	}

}
