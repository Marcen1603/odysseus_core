package de.uniol.inf.is.odysseus.wsenrich.util;

import org.apache.http.HttpEntity;

public interface IConnectionForWebservices {
	
	/**
	 * Method to connect to a HTTP Entpoint
	 * @param the charset of the retrieved data
	 * @param the contentType of the retrieved data, eg. XML or JSON
	 */
	public void connect(String charset, String contentType);
	
	/**
	 * Method to close the HTTP Endpoint connection
	 */
	public void closeConnection();
	
	/**
	 * Retrieves the Status line of the Response
	 * @return the Status line
	 */
	public String retrieveStatusLine();
	
	/**
	 * Retrieves the Body of the HTTP Response
	 * @return the Body as a HTTPEntity
	 */
	public HttpEntity retrieveBody(); 
	
	/**
	 * @return The uri of the Http Request
	 */
	public String getUri();
	
	/**
	 * Adds a Header Element for the Http Request
	 * @param arguments The Name of the Header-Element
	 * @param value The value of the Header-Elements
	 * Example: argument = "Content-Type"
	 * 			value = "UTF-8"
	 */
	public void addHeader(String argument, String value);
	
	/**
	 * Setter for the uri
	 * @param uri the uri for the request
	 */
	public void setUri(String uri);
	
	/**
	 * Setter for the Arguments
	 * @param value The arguments
	 */
	public void setArguments(String value);
	
	/**
	 * @return The arguments of the request or the Document of a Post-Request
	 */
	public String getArguments();
	/**
	 * @return The name of the Connection method
	 */
	public String getName();
	
	/**
	 * Creates a instance of a class who implements this class
	 */
	public IConnectionForWebservices createInstance();
}
