package de.uniol.inf.is.odysseus.wsenrich.util;


import de.uniol.inf.is.odysseus.wsenrich.exceptions.DatafieldNotFoundException;

public class XmlKeyFinder implements IKeyFinder {
	
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
	
	public XmlKeyFinder() {
		//Needed for the KeyFinderRegistry
		
	}
	
	/**
	 * Constructor for the XmlKeyFinder. It searches the given message
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
	
	@Override
	public String getSearch() {
		
		return this.search;
	}
	
	@Override
	public void setSearch(String search) {
		
		this.search = search;
	}
	
	@Override
	public String getMessage() {
		
		return this.message.toString();
	}
	
	@Override
	public void setMessage(String message) {
		
		this.message = new StringBuffer(message);
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
	
	@Override
	public Object getValueOf(String search) throws DatafieldNotFoundException {
		
		cutStartofMessage();
		cutEndofMessage();
		if(!this.search.equals(search)) {
			this.search = search;
		}
		
		int match = this.message.indexOf(search);
		
		if(match == -1) {
			throw new DatafieldNotFoundException();
		} else {
			int endOfStartElement = this.message.indexOf(ENDTAG.toString(), match);
			int startOfEndElement = this.message.indexOf(STARTTAG.toString(), endOfStartElement + 1);
			String temp = this.message.substring(endOfStartElement + 1, startOfEndElement);
			this.value = temp;
		}
		return value;
	}

	@Override
	public String getName() {
		return "XML";
	}

	@Override
	public IKeyFinder createInstance() {
		return new XmlKeyFinder();
	}


}
