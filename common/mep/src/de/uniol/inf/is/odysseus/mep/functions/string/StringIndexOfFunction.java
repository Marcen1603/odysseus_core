package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StringIndexOfFunction extends AbstractBinaryStringFunction<Integer> {

	private static final long serialVersionUID = 1786040683554351893L;

	public StringIndexOfFunction() {
		super("indexof", SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return a.indexOf(b);
	}

}
