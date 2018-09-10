package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ConcatFunction extends AbstractFunction<String> {

	private static final long serialVersionUID = -2667259091974125547L;

	public ConcatFunction() {
		super("concat", 2, getAllTypes(2), SDFDatatype.STRING);
	}
	
	@Override
	public String getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(a);
		sb.append(b);
		return sb.toString();
	}

}
