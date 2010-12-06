package de.uniol.inf.is.odysseus.datamining.builder;

public class AttributeOutOfRangeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3632365610334662268L;
	private String attributeName;
	private String errorMessage;


	public AttributeOutOfRangeException(String attribteName, String errorMessage) {
		this.attributeName = attribteName;
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {

		return "The value of " + attributeName +" "+ errorMessage;
	}

}
