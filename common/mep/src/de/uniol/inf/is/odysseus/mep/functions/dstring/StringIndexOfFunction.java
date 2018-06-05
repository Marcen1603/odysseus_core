package de.uniol.inf.is.odysseus.mep.functions.dstring;

import de.uniol.inf.is.odysseus.core.datatype.DString;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StringIndexOfFunction extends AbstractBinaryDStringFunction<Integer> {

	private static final long serialVersionUID = 1786040683554351893L;

	public StringIndexOfFunction() {
		super("indexof", SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		DString op1 = getInputValue(0);
		String op2 = getInputValue(1).toString();
		return op1.indexOf(op2);
	}

}
