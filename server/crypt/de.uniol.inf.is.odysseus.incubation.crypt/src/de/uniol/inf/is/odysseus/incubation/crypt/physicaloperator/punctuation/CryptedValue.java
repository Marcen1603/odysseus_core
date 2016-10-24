package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation;

/**
 * This is a wrapper for two values.<br>
 * You could use it for storing the old and the new value of an ecrypted value.
 * 
 * @author MarkMilster
 *
 */
public class CryptedValue {

	private String oldValue;
	private String newValue;

	/**
	 * Constuctor
	 * 
	 * @param oldValue
	 *            The oldValue, before crypting
	 * @param newValue
	 *            The newValue, after crypting
	 */
	public CryptedValue(String oldValue, String newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Returns the old Value
	 * 
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * Sets the oldValue
	 * 
	 * @param oldValue
	 *            the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * Returns the newValue
	 * 
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * Sets the newValue
	 * 
	 * @param newValue
	 *            the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
