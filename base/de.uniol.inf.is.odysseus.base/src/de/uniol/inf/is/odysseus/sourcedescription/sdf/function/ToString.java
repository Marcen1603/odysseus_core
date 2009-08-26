package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;

public class ToString extends CustomFunction {

	public ToString() {
		super();
		numberOfParameters = 1;
	}

	@Override
	public String getName() {
		return "ToString";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);
		stack.push(stack.pop().toString());
	}

}
