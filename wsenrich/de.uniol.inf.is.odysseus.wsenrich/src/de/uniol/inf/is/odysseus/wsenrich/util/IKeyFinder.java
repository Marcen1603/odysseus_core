package de.uniol.inf.is.odysseus.wsenrich.util;

import de.uniol.inf.is.odysseus.wsenrich.exceptions.DatafieldNotFoundException;

public interface IKeyFinder {
	
	/**
	 * @return the searched element
	 */
	public String getSearch();
	
	/**
	 * Setter for the searched element
	 * @param search the searched element
	 */
	public void setSearch(String search);
	
	/**
	 * @return the message
	 */
	public String getMessage();
	
	/**
	 * Setter for the message
	 * @param Message the message
	 */
	public void setMessage(String Message);
	
	/**
	 * searches the message for the first occurence of the search and
	 * returns the value of the element
	 * @param search the element name to search for
	 * @return the value of the searched element
	 * @throws DatafieldNotFoundException if the searched element ist not in the message
	 */
	public Object getValueOf(String search) throws DatafieldNotFoundException;

}
