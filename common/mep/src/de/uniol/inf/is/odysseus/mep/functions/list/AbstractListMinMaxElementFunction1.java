package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractListMinMaxElementFunction1 extends AbstractFunction<Object> {

	private static final long serialVersionUID = 758116340731496107L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.LIST }};

	
	public AbstractListMinMaxElementFunction1(String name){
		super(name,1, accTypes, SDFDatatype.OBJECT, false);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes"})
	public Object getValue() {
		List<Comparable> list = getInputValue(0);
		Comparable ret = list.get(0);
		for (Comparable t:list){
			if (compare(ret, t)){
				ret = t;
			}
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	abstract protected boolean compare(Comparable val1, Comparable val2);



	
}