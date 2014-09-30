package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListContainsFunction extends AbstractFunction<Boolean> {
	
	private static final long serialVersionUID = -871096326523245775L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.SIMPLE_TYPES,{SDFDatatype.LIST} };

	public ListContainsFunction() {
		super("contains", 2, accTypes, SDFDatatype.BOOLEAN, false);
	}
	
	@Override
	public Boolean getValue() {
		Object o = getInputValue(0);
		@SuppressWarnings("rawtypes")
		List l = (List) getInputValue(1);
		return l.contains(o);
	}

}
