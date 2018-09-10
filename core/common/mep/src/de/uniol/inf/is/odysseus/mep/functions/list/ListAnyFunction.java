package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

public class ListAnyFunction extends AbstractLambdaListBooleanFunction {

	private static final long serialVersionUID = 1686162833740088462L;

	public ListAnyFunction() {
		super("Any");
	}

	@Override
	protected Boolean calcValue(List<Boolean> out) {
		for (Boolean object : out) {
			if (object){
				return true;
			}
		}
		return false;
	}

}
