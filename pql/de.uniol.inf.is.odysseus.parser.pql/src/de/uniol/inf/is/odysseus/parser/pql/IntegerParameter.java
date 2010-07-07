package de.uniol.inf.is.odysseus.parser.pql;

public class IntegerParameter extends AbstractParameter<Integer> {

	public IntegerParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	public void setValueOf(Object object) {
		int value = ((Long) object).intValue();
		setValue(value);
	}

}
