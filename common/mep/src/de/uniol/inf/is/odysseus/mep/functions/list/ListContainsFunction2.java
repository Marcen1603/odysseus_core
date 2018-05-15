package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * This function allows to find out, if an object or a string is contained in a list
 * @author Marco Grawunder
 *
 */
public class ListContainsFunction2 extends AbstractFunction<Boolean> {
	
	private static final long serialVersionUID = -871096326523245775L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {{SDFDatatype.LIST}, {SDFDatatype.STRING, SDFDatatype.OBJECT} };

	public ListContainsFunction2() {
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
