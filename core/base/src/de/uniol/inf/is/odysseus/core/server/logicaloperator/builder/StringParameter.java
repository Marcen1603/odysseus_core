package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;


public class StringParameter extends AbstractParameter<String> {

	private static final long serialVersionUID = -5895025314868405137L;

	public StringParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}
	
	public StringParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public StringParameter() {
		super();
	}

	@Override
	protected void internalAssignment() {
		setValue((String) this.inputValue);
	}

}
