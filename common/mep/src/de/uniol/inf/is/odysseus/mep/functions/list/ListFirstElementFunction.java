package de.uniol.inf.is.odysseus.mep.functions.list;

import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListFirstElementFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -3282877303737235603L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.LISTS};

	public ListFirstElementFunction() {
		super("first", 1, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		List<Object> l = getInputValue(0);
		int pos = 0;
				
		return l.get(pos);
	}
	
	@Override
	public SDFDatatype determineType(IExpression<?>[] args) {
		if (args != null){
			return args[0].getReturnType().getSubType();
		}
		throw new IllegalArgumentException("Types cannot be determined with "+args);
	}
	
	@Override
	public boolean determineTypeFromInput() {
		return true;
	}
	

}
