package de.uniol.inf.is.odysseus.datamining.builder;

public class AttributeOutfRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632365610334662268L;
	private String attributeName;
	private int min;
	private int max;

	public AttributeOutfRangeException(String attribteName, int min, int max) {
		this.attributeName = attribteName;
		this.min = min;
		this.max = max;
	}

	@Override
	public String getMessage() {

		return "The value of " + attributeName + " has to be between " + min
				+ " and " + max + ".";
	}

}
