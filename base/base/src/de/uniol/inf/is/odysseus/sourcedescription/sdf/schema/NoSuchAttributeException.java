/**
 * 
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class NoSuchAttributeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2882707151634648226L;

	private final String attribute;

	/**
	 * @param message
	 */
	public NoSuchAttributeException(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

}
