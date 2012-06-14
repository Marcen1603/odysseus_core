package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

public class LongParameter extends AbstractParameter<Long> {

	private static final long serialVersionUID = -5400366392737895103L;

	public LongParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}
	
	public LongParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public LongParameter() {
		super();
	}

	@Override
	protected void internalAssignment() {
		setValue((Long) this.inputValue);
	}

}
