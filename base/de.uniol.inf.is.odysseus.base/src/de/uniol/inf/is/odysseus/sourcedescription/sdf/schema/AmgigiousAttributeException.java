/**
 * 
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class AmgigiousAttributeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 795606483718194631L;

	private final String attribute;

	/**
	 * @param message
	 * @param cause
	 */
	public AmgigiousAttributeException(String attribute) {
		super();
		this.attribute = attribute;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

}
