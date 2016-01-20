package de.uniol.inf.is.odysseus.mep.functions.string;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class StringRegExFunction extends AbstractBinaryStringFunction<Boolean> {

	private static final long serialVersionUID = -6968148110070018724L;
	private static final Map<StringRegExFunction, Pattern> patterns = new ConcurrentHashMap<>();
	
	public StringRegExFunction() {
		super("regex", SDFDatatype.BOOLEAN, false);
	}
	
	@Override
	public Boolean getValue() {
		String a = getInputValue(0);
		String b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		if (!patterns.containsKey(this)) {
			patterns.put(this, Pattern.compile(b));
		}
		return patterns.get(this).matcher(a).matches();
	}

}
