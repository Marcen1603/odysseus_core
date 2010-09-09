package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;

public class ToNumber extends CustomFunction {

	public ToNumber() {
		super();
		numberOfParameters = 1;
	}

	@Override
	public String getName() {
		return "ToNumber";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);

		Object obj = stack.pop();
		if (obj instanceof String) {
			stack.push(Double.parseDouble((String) obj));
			return;
		}

		System.out.println(obj.getClass());
		
		throw new ParseException("invalid parameter type in: " + getName());
	}

}
