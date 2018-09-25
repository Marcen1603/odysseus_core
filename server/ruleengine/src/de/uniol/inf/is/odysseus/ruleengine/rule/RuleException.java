package de.uniol.inf.is.odysseus.ruleengine.rule;

public class RuleException extends Exception {

	private static final long serialVersionUID = -614785735177806867L;

	public RuleException(String arg0) {
		super(arg0);
	}

	public RuleException(Throwable arg0) {
		super(arg0);
	}

	public RuleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RuleException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
