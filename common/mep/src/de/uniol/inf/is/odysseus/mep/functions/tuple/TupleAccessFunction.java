package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class TupleAccessFunction extends AbstractFunction<Object> implements IHasAlias{
	

	private static final long serialVersionUID = -6751879119439264427L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.TUPLE}, SDFDatatype.NUMBERS};

	public TupleAccessFunction() {
		super("[]", 2, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		Tuple<?> l = (Tuple<?>) getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
				
		return l.getAttribute(pos);
	}

	@Override
	public String getAliasName() {
		return "elementAt";
	}
	
	

}
