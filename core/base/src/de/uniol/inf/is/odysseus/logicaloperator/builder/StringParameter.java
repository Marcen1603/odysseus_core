package de.uniol.inf.is.odysseus.logicaloperator.builder;


public class StringParameter extends AbstractParameter<String> {

	private static final long serialVersionUID = -5895025314868405137L;

	public StringParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	public StringParameter() {
		super();
	}

	@Override
	protected void internalAssignment() {
		setValue((String) this.inputValue);
	}

}
