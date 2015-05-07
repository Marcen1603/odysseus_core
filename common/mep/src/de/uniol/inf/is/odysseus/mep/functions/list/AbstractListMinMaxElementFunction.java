package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractListMinMaxElementFunction extends AbstractFunction<Tuple<?>> {

	private static final long serialVersionUID = 758116340731496107L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.LIST }, { SDFDatatype.INTEGER} };

	
	public AbstractListMinMaxElementFunction(String name){
		super(name,2, accTypes, SDFDatatype.OBJECT, false);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes"})
	public Tuple<?> getValue() {
		List<Tuple<?>> list = getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
		Tuple<?> ret = list.get(0);
		Comparable val = ret.getAttribute(pos);
		for (Tuple<?> t:list){
			if (compare(pos, val, t)){
				val = t.getAttribute(pos);
				ret = t;
			}
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	abstract protected boolean compare(int pos, Comparable val1, Tuple<?> t);



	
}