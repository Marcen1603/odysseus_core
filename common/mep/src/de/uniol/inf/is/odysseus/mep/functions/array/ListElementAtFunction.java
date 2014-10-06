package de.uniol.inf.is.odysseus.mep.functions.array;

import java.util.List;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ListElementAtFunction extends AbstractFunction<Object> implements IHasAlias{

	private static final long serialVersionUID = -3282877303737235603L;

	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.LISTS, SDFDatatype.NUMBERS};

	public ListElementAtFunction() {
		super("[]", 2, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		List<Object> l = getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
				
		return l.get(pos);
	}

	@Override
	public String getAliasName() {
		return "elementAt";
		//return "listElementAt";
	}
	
	@Override
	public boolean determineTypeFromFirstInput() {
		return true;
	}

	
}
