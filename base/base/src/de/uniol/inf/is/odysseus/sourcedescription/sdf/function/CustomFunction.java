package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import org.nfunk.jep.function.PostfixMathCommand;

public abstract class CustomFunction extends PostfixMathCommand {

	public CustomFunction() {
		super();
	}

	public abstract String getName();

}
