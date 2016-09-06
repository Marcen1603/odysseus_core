/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

/**
 * @author MarkMilster
 *
 */
public class StringMessage {
	
	private String string;
	
	public StringMessage(String string) {
		this.string = string;
	}

	/**
	 * @return the string
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string the string to set
	 */
	public void setString(String string) {
		this.string = string;
	}

}
