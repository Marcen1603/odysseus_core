package de.uniol.inf.is.odysseus.datamining.builder;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * This exception occurs if an attribute's data type is not numeric.
 * 
 * @author Kolja Blohm
 * 
 */
public class NonNumericAttributeException extends Exception {

	private static final long serialVersionUID = -5590493141116859811L;
	private SDFAttribute attribute;

	/**
	 * Creates a new instance of NonNumericAttributeException for the specified
	 * attribute
	 * 
	 * @param attribute
	 *            the non numeric attribute
	 */
	public NonNumericAttributeException(SDFAttribute attribute) {
		this.attribute = attribute;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {

		return "The data type of " + attribute.getAttributeName()
				+ " has to be numeric.";
	}
}
