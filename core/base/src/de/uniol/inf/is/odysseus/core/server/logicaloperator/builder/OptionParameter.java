package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;


public class OptionParameter extends AbstractParameter<Option> {

	private static final long serialVersionUID = 3654821505140630923L;

	public OptionParameter(String name, REQUIREMENT requirement) {
		super(name, requirement, USAGE.RECENT);
	}
	
	public OptionParameter(String name, REQUIREMENT requirement, USAGE usage) {
		super(name, requirement, usage);
	}

	public OptionParameter() {
		super();
	}
	
	@Override
	protected void internalAssignment() {
		Option option = new Option((String)((List<?>)inputValue).get(0),(String)((List<?>)inputValue).get(1));
		setValue((Option)option);
	}

	@Override
	protected String getPQLStringInternal() {
		return "['"+getValue().name+"','"+getValue()+"]";
	}

}
