package de.uniol.inf.is.odysseus.wsenrich.util;


import de.uniol.inf.is.odysseus.wsenrich.exceptions.DatafieldNotFoundException;

public class XmlKeyFinder {
	
	/**
	 * The Start of a Element
	 */
	private static final Character STARTTAG = '<';
	
	/**
	 * The End of a Element
	 */
	private static final Character ENDTAG = '>';
	
	/**
	 * The Starttag of the Soap-Body
	 */
	private static final String BODY_STARTTAG = "<soap:Body>";
	
	/**
	 * The Endtag of the Soap-Body
	 */
	private static final String BODY_ENDTAG = "</soapenv:Body>";
	
	/**
	 * The message 
	 */
	private StringBuffer message;
	
	/**
	 * The searched element
	 */
	private String search;
	
	/**
	 * The value of the searched Element
	 */
	private Object value;
	
	/**
	 * Constructor for the XmlKeyFinder. It searched the given message
	 * for the first occurence of the search and returns the value of
	 * the element
	 * @param message the xml message
	 * @param search the searched element
	 */
	public XmlKeyFinder(String message, String search) {
		
		this.message = new StringBuffer(message);
		this.search = search;
		cutStartofMessage();
		cutEndofMessage();
	}
	
	/**
	 * @return the searched element
	 */
	public String getSearch() {
		
		return this.search;
	}
	
	/**
	 * Setter for the searched element
	 * @param search the searched element
	 */
	public void setSearch(String search) {
		
		this.search = search;
	}
	
	/**
	 * Cuts the start of the message (if there´s content before data)
	 * Example: If the message is a soap message, this method cuts all
	 * meta informations
	 */
	private void cutStartofMessage() {
		
		int endOfStartMessage = this.message.indexOf(BODY_STARTTAG);
		if(endOfStartMessage != -1) {
			this.message.delete(0, endOfStartMessage);
		}		
	}
	
	/**
	 * cuts the end of a message (if there´s content after data)
	 * Example: If the message is a soap message, this method cuts
	 * all meta informations after data
	 */
	private void cutEndofMessage() {
		
		int startOfEndMessage = this.message.indexOf(BODY_ENDTAG);
		if(startOfEndMessage != -1) {
			this.message.delete(startOfEndMessage, this.message.length());
		}
		
	}
	
	/**
	 * searches the message for the first occurence of the search and
	 * returns the value of the element
	 * @param search the element name to search for
	 * @return the value of the searched element
	 * @throws DatafieldNotFoundException if the searched element ist not in the message
	 */
	public Object getValueOf(String search) throws DatafieldNotFoundException {
		
		if(!this.search.equals(search)) {
			this.search = search;
		}
		int match = this.message.indexOf(STARTTAG + search);
		
		if(match == -1) {
			throw new DatafieldNotFoundException();
			
		} else {
			int endOfElement = this.message.indexOf(ENDTAG.toString(), match);
			int endOfValue = this.message.indexOf(ENDTAG.toString(), endOfElement + 1);
			String temp = this.message.substring(endOfElement + 1, endOfValue - search.length() - 2);
			this.value = temp;
			
		}
		return value;
	}

}
