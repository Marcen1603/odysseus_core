package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class IntegerParameter extends AbstractParameter<Integer> {

	public IntegerParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		int value = ((Long) inputValue).intValue();
		setValue(value);
	}

}
