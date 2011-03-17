package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class LongParameter extends AbstractParameter<Long> {

	private static final long serialVersionUID = -5400366392737895103L;

	public LongParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	public LongParameter() {
		super();
	}

	@Override
	protected void internalAssignment() {
		setValue((Long) this.inputValue);
	}

}
