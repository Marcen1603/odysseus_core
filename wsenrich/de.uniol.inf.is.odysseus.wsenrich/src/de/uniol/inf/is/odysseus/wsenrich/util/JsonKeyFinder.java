package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonKeyFinder implements ContentHandler {

	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(JsonKeyFinder.class);
	
	/**
	 * The Value of the searched Datafield
	 */
	private Object value;
	
	/**
	 * returns true if a element with the specified name was found
	 */
	private boolean found = false;
	
	/**
	 * true if the end of the Json Objects is archieved
	 */
	private boolean end = false;
	
	/**
	 * The name of a Json Datafield
	 */
	private String key;
	
	/**
	 * The name of the Datafield to seach for
	 */
	private String matchKey;
	
	/**
	 * Sets the key to search for in the Json Object
	 * @param matchKey the key to search for
	 */
	public void setMatchKey(String matchKey) {
		
		this.matchKey = matchKey;
	}
	
	/**
	 * @return the value of the searched Datafield
	 */
	public Object getValue() {
		
		return this.value;
	}
	
	/**
	 * @return true if the end of the Json Object is archieved
	 */
	public boolean isEnd() {
		
		return this.end;
	}
	
	/**
	 * @return true if a datafield with the specified name was found
	 */
	public boolean isFound() {
		
		return this.found;
	}
	
	@Override
	public boolean endArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void endJSON() throws ParseException, IOException {
		
		this.end = true;
		
		if(this.value == null && this.end) {
			logger.error("The specified dataelement was not found in the Json Object");
		}
		
	}

	@Override
	public boolean endObject() throws ParseException, IOException {
	
		return true;
	}

	@Override
	public boolean endObjectEntry() throws ParseException, IOException {

		return true;
	}

	@Override
	public boolean primitive(Object arg0) throws ParseException, IOException {
		
		if(key != null) {
			if(key.equals(matchKey)) {
			this.found = true;
			this.value = arg0;
			return false;
			}
		}
		return true;
	}

	@Override
	public boolean startArray() throws ParseException, IOException {
	
		return true;
	}

	@Override
	public void startJSON() throws ParseException, IOException {
		
		this.found = false;
		this.end = false;
		
	}

	@Override
	public boolean startObject() throws ParseException, IOException {

		return true;
	}

	@Override
	public boolean startObjectEntry(String arg0) throws ParseException,
			IOException {
		
		this.key = arg0;
		return true;

	}

}
