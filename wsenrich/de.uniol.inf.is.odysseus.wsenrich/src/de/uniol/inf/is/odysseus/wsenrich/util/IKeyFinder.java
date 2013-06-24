package de.uniol.inf.is.odysseus.wsenrich.util;

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
	 * @param filterNullTuples: If true, a null-response (No Data) will not be returned
	 * @return the value of the searched element
	 */
	public Object getValueOf(String search, boolean keyValue);
	
	/**
	 * @return the name of the implementation of the IKeyFinder Interface
	 */
	public String getName();
	
	/**
	 * Creates a instance of a class who implements this class
	 */
	public IKeyFinder createInstance();

}
