package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

public class ListFilterFunction extends AbstractLambdaListFunction{

	private static final long serialVersionUID = -8269748747594828192L;


	public ListFilterFunction() {
		super("filter");
	}

	@Override
	protected void fillReturnList(List<Object> out, Object o) {
		if ((boolean)expression.getValue()) {
			out.add(o);
		}
	}
}
