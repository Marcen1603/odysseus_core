package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListNumericContainsFunction extends AbstractFunction<Boolean> {
	
	private static final long serialVersionUID = -871096326523245775L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS,{SDFDatatype.LIST} };

	public ListNumericContainsFunction() {
		super("contains", 2, accTypes, SDFDatatype.BOOLEAN, false);
	}
	
	@Override
	public Boolean getValue() {
		Number o = getInputValue(0);
		@SuppressWarnings({ "unchecked" })
		List<Number> l = (List<Number>) getInputValue(1);
		for (Number object : l) {
			if (object.doubleValue() == o.doubleValue()){
				return true;
			}
		}
		return false;
	}

}
