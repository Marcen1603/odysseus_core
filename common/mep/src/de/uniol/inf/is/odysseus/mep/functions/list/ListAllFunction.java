package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

public class ListAllFunction extends AbstractLambdaListBooleanFunction {

	private static final long serialVersionUID = 610318523593242568L;

	public ListAllFunction() {
		super("All");
	}

	@Override
	protected Boolean calcValue(List<Boolean> out) {
		for (Boolean object : out) {
			if (!object){
				return false;
			}
		}
		return true;
	}

}
