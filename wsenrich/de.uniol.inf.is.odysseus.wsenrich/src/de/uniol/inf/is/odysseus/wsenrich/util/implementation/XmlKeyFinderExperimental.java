package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IKeyFinder;

public class XmlKeyFinderExperimental implements IKeyFinder {
	
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
	
	public XmlKeyFinderExperimental() {
		//Needed for the KeyFinderRegistry	
	}
	
	/**
	 * Constructor for the XmlKeyFinder. It searches the given message
	 * for the first occurrence of the search and returns the value of
	 * the element
	 * @param message the xml message
	 * @param search the searched element
	 */
	public XmlKeyFinderExperimental(String message, String search) {
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
	public void setMessage(String message, String charset, boolean multiTupleOutput) {
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
	public Object getValueOf(String search, boolean keyValue, int tupleCount) {
		StringBuffer temp = new StringBuffer();
		cutStartofMessage();
		cutEndofMessage();
		if(!this.search.equals(search)) {
			this.search = search;
		}
		int match = this.message.indexOf(this.search);
		if(match == -1) {
			return null;
		}		
		if(keyValue) {
			temp.append(search + " : ");
			int endOfStartElement = this.message.indexOf(ENDTAG.toString(), match);
			int startOfEndElement = this.message.indexOf(STARTTAG.toString(), endOfStartElement + 1);
			temp.append(this.message.substring(endOfStartElement + 1, startOfEndElement));
		} else {
			int endOfStartElement = this.message.indexOf(ENDTAG.toString(), match);
			int startOfEndElement = this.message.indexOf(STARTTAG.toString(), endOfStartElement + 1);
			temp.append(this.message.substring(endOfStartElement + 1, startOfEndElement));
		}
		this.value = temp;
		return value;
	}

	@Override
	public String getName() {
		return "XMLEXPERIMENTAL";
	}

	@Override
	public IKeyFinder createInstance() {
		return new XmlKeyFinderExperimental();
	}

	@Override
	public int getTupleCount() {
		return 1;
	}
}
