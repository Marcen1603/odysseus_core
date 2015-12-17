package de.uniol.inf.is.odysseus.mep.functions.dstring;

import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StringRegExFunction extends AbstractBinaryDStringFunction<Boolean> {

	private static final long serialVersionUID = -6968148110070018724L;
	private Pattern pattern;
	
	public StringRegExFunction() {
		super("regex", SDFDatatype.BOOLEAN, false);
	}
	
	@Override
	public Boolean getValue() {
		String value = getInputValue(0).toString();
		if (pattern == null){
			pattern = Pattern.compile(getInputValue(1).toString());
		}
		return pattern.matcher(value).matches();
	}

}
