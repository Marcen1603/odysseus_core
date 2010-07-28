package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class MissingParameterException extends Exception {

	private static final long serialVersionUID = 7569100991623331316L;

	public MissingParameterException(String name) {
		super("missing parameter " + name);
	}

}
