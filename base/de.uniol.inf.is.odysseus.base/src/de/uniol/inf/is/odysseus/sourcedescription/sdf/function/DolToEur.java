package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;

public class DolToEur extends CustomFunction {

	private static double EXCHANGERATE = 1d / 1.55d;

	public DolToEur() {
		numberOfParameters = 1;
	}

	@Override
	public String getName() {
		return "DolToEur";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);

		double value = ((Number) stack.pop()).doubleValue();
		value *= EXCHANGERATE;
		stack.push(value);
	}

	public static void setExchangeRate(double value) {
		EXCHANGERATE = value;
	}

}
