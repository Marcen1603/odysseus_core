package de.uniol.inf.is.odysseus.wsenrich.util;

public interface ISoapMessageCreator {
	
	/**
	 * @return the generated soap-message of the operation name
	 */
	public String getSoapMessage();
	
	/**
	 * @return the operation name of the SoapMessage
	 */
	public String getOperationName();
	
	/**
	 * @return The Name of the concrete Implementation of this Interface
	 */
	public String getName();
	
	/**
	 * Builds the SoapMessage
	 */
	public void buildSoapMessage();
	
	/**
	 * @return a Instance of the concrete Implementation of this Interface
	 */
	public ISoapMessageCreator createInstance(String wsdlLocation, String operation);
}
