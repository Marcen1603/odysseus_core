package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListMaxElementFunction extends AbstractFunction<Tuple<?>> {

	private static final long serialVersionUID = 758116340731496107L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.LIST }, { SDFDatatype.INTEGER} };

	
	public ListMaxElementFunction(){
		super("max",2, accTypes, SDFDatatype.OBJECT, false);
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Tuple<?> getValue() {
		List<Tuple<?>> list = getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
		Tuple<?> ret = list.get(0);
		Comparable max = ret.getAttribute(pos);
		for (Tuple<?> t:list){
			if (max.compareTo((Comparable)t.getAttribute(pos)) < 0){
				max = t.getAttribute(pos);
				ret = t;
			}
		}
		return ret;
	}

	
}