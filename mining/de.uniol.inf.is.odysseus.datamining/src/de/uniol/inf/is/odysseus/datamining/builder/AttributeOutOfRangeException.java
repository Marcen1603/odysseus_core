package de.uniol.inf.is.odysseus.datamining.builder;

/**
 * This exception occurs if an attribute's value is out of it's allowed range.
 * 
 * @author Kolja Blohm
 * 
 */
public class AttributeOutOfRangeException extends Exception {

	private static final long serialVersionUID = 3632365610334662268L;
	private String attributeName;
	private String errorMessage;

	/**
	 * Creates a new instance of AttributeOutOfRangeException with an error
	 * message and the name of the attribute.
	 * 
	 * @param attributeName
	 *            the name of the attribute
	 * @param errorMessage
	 *            the error message
	 */
	public AttributeOutOfRangeException(String attributeName,
			String errorMessage) {
		this.attributeName = attributeName;
		this.errorMessage = errorMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		return "The value of " + attributeName + " " + errorMessage;
	}

}
