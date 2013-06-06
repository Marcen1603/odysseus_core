package de.uniol.inf.is.odysseus.wsenrich.util;

import org.apache.http.HttpEntity;

public interface IConnectionForWebservices {
	
	/**
	 * Method to connect to a HTTP Entpoint
	 */
	public void connect();
	
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
	 * @param value The value of the Header-Elemente
	 * Example: argument = "Content-Type"
	 * 			value = "UTF-8"
	 */
	public void addHeader(String argument, String value);
	

}
