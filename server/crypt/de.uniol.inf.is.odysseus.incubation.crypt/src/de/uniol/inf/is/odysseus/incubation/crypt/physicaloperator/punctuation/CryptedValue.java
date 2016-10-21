package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation;

/**
 * @author MarkMilster
 *
 */
public class CryptedValue {

	private String oldValue;
	private String newValue;

	public CryptedValue(String oldValue, String newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return the oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue
	 *            the oldValue to set
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue
	 *            the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
