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
	
	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		if( inputValue instanceof Option ) {
			setValue( (Option)inputValue );
			return;
		}
		
		List<String> list = (List<String>) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException("Wrong number of inputs for Option. Expecting name and value.");
		}
		Option option = new Option(list.get(0), list.get(1));
		setValue(option);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String getPQLStringInternal() {
		if( inputValue instanceof List ) {
			List<String> list = (List<String>) inputValue;
			return "['" + list.get(0) + "','" + list.get(1) + "']";
		} else if ( inputValue instanceof Option ) {
			Option opt = (Option)inputValue;
			return "['" + opt.name + "','" + opt.value + "']";
		}
		
		return "";
	}

}
