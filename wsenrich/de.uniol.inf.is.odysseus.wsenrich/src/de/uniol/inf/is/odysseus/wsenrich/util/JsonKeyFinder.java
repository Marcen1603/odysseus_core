package de.uniol.inf.is.odysseus.wsenrich.util;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.wsenrich.exceptions.DatafieldNotFoundException;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;

public class JsonKeyFinder implements IKeyFinder {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(WSEnrichAO.class);
	
	/**
	 * The Json Parser Object
	 */
	private JSONParser parser;
	
	/**
	 * The ContentHandler Object for Json Data
	 */
	private JsonContentHandler reader;
	
	/**
	 * The Json Message
	 */
	private String message;
	
	/**
	 * The searched Element
	 */
	private String search;
	
	/**
	 * The value of the searched Element
	 */
	private Object value;
	
	/**
	 * Constructor for the JsonKeyFinder. It searched the given message
	 * for the first occurence of the search and returns the value of
	 * the element
	 * @param message the json message
	 * @param search the searched element
	 */
	public JsonKeyFinder(String message, String search) {
		
		this.parser = new JSONParser();
		this.reader = new JsonContentHandler();
		this.message = message;
		this.search = search;
			
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
		return this.message;
	}
	
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Object getValueOf(String search) throws DatafieldNotFoundException {
		
		if(!this.search.equals(search)) 
			this.search = search;
		
		reader.setMatchKey(this.search);
		
		try {
		
			while(!reader.isFound()) {
				parser.parse(message, reader, true);
			
				if(reader.isFound()) {
					this.value = reader.getValue();
					return this.value;
				}
				
				if(reader.isEnd()) {
					throw new DatafieldNotFoundException();
				}
			}
		} catch (ParseException pe) {
			logger.error("Ecxception while parsing Json Document. Cause: {}", pe.getMessage());
		}
		return null;
	}

}
