package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.io.Serializable;

/**
 * Message to send a String. It could be used for simple messages or testing.
 * 
 * @author MarkMilster
 *
 */
public class StringMessage implements Serializable {

	private static final long serialVersionUID = -6713597515643506405L;
	private String string;

	/**
	 * Default constructor to parse in JSON
	 */
	public StringMessage() {

	}

	/**
	 * Constructor with given String to save in this message
	 * 
	 * @param string String to save in this message
	 */
	public StringMessage(String string) {
		this.string = string;
	}

	/**
	 * Returns the String
	 * 
	 * @return the string
	 */
	public String getString() {
		return string;
	}

	/**
	 * Sets the String
	 * 
	 * @param string
	 *            the string to set
	 */
	public void setString(String string) {
		this.string = string;
	}

}
