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
		StringBuilder sb = new StringBuilder();
		sb.append(getInputValue(0));
		sb.append(getInputValue(1));
		return sb.toString();
	}

}
