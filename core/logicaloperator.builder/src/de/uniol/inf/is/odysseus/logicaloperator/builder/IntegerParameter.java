package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class IntegerParameter extends AbstractParameter<Integer> {
	
	private static final long serialVersionUID = -7077149501557833637L;

	public IntegerParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		int value = ((Long) inputValue).intValue();
		setValue(value);
	}

}
