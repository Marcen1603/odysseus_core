package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;

public class Now extends CustomFunction {
	public Now() {
		super();
		numberOfParameters = 0;
	}

	@Override
	public String getName() {
		return "Now";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Stack stack) throws ParseException {
		stack.push(new Double(System.currentTimeMillis()));
	}

}
