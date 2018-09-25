package de.uniol.inf.is.odysseus.mep.functions.list;

import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class ListMinElementFunction extends AbstractListMinMaxElementFunction{

	private static final long serialVersionUID = 758116340731496107L;

	
	public ListMinElementFunction(){
		super("min");
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected boolean compare(int pos, Comparable val1, Tuple<?> t) {
		return val1.compareTo(t.getAttribute(pos)) > 0;
	}
	
}