package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;

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
		if (inputValue instanceof Option) {
			setValue((Option) inputValue);
			return;
		}

		@SuppressWarnings("rawtypes")
		List list = (List) inputValue;
		if (list.size() != 2) {
			throw new IllegalArgumentException("Wrong number of inputs for Option. Expecting name and value.");
		}
		Option option = new Option((String) list.get(0), list.get(1));
		setValue(option);
	}

	@Override
	protected String getPQLStringInternal() {
		if (inputValue instanceof List) {
			@SuppressWarnings("rawtypes")
			List list = (List) inputValue;
			if (list.get(1) instanceof Number) {
				return "['" + list.get(0) + "'," + list.get(1) + "]";
			} else {
				return "['" + list.get(0) + "','" + list.get(1) + "']";
			}
		} else if (inputValue instanceof Option) {
			Option opt = (Option) inputValue;
			if (opt.getValue() instanceof Number) {
				return "['" + opt.getName() + "'," + opt.getValue() + "]";
			} else {
				return "['" + opt.getName() + "','" + opt.getValue() + "']";
			}
		}

		return "";
	}

}
