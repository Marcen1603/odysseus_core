package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ConcatFunction extends AbstractFunction<DString> {

	private static final long serialVersionUID = -2667259091974125547L;

	public ConcatFunction() {
		super("concat", 2, getAllTypes(2), SDFDatatype.STRING);
	}
	
	@Override
	public DString getValue() {
		StringBuilder sb = new StringBuilder();
		sb.append(DString.valueOf(getInputValue(0)));
		sb.append(DString.valueOf(getInputValue(1)));
		return new DString(sb.toString());
	}

}
