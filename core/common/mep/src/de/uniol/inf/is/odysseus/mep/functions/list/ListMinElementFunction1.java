package de.uniol.inf.is.odysseus.mep.functions.list;

public class ListMinElementFunction1 extends AbstractListMinMaxElementFunction1 {

	private static final long serialVersionUID = 3875344825004654777L;

	public ListMinElementFunction1(){
		super("min_");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected boolean compare(Comparable val1, Comparable val2) {
		return val1.compareTo(val2) > 0;
	}

}
