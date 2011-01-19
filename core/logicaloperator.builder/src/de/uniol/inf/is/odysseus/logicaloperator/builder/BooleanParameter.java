package de.uniol.inf.is.odysseus.logicaloperator.builder;

public class BooleanParameter extends AbstractParameter<Boolean> {
	
	private static final long serialVersionUID = -7491596371995854348L;

	public BooleanParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);		
	}

	@Override
	protected void internalAssignment() {
		boolean value = Boolean.parseBoolean(inputValue.toString());		
		setValue(value);
		
	}

}
