package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

public class ListLastElementFunction extends AbstractListPosElementFunction {

	private static final long serialVersionUID = -3282877303737235603L;

	public ListLastElementFunction() {
		super("last");
	}

	@Override
	protected int getPos(List<Object> l) {
		return l.size()-1;
	}
		
}
