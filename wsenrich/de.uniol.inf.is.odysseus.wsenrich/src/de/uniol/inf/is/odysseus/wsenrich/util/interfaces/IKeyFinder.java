package de.uniol.inf.is.odysseus.wsenrich.util.interfaces;

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
	 * @param charset the the charset of the message
	 * @param multiTupleOutput 
	 */
	public void setMessage(String Message, String charset, boolean multiTupleOutput);
	
	/**
	 * searches the message for the first occurrence of the search and
	 * returns the value of the element
	 * @param search the element name to search for
	 * @param keyValue: If true, a null-response (No Data) will not be returned
	 * @param tupleCount: how many tuples will be created from the webservice response
	 * @return the value of the searched element
	 */
	public Object getValueOf(String search, boolean keyValue, int tupleCount);
	
	/**
	 * @return the count of tuples that will be created from the webservice response
	 */
	public int getTupleCount();
	
	/**
	 * @return the name of the implementation of the IKeyFinder Interface
	 */
	public String getName();
	
	/**
	 * Creates a instance of a class who implements this class
	 */
	public IKeyFinder createInstance();
}
