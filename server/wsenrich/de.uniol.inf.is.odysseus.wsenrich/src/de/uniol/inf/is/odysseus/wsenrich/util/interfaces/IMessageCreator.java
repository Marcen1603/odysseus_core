package de.uniol.inf.is.odysseus.wsenrich.util.interfaces;

public interface IMessageCreator {
	
	/**
	 * @return the generated soap-message of the operation name
	 */
	public String getMessage();
	
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
	public void buildMessage();
	
	/**
	 * @return a Instance of the concrete Implementation of this Interface
	 */
	public IMessageCreator createInstance(String wsdlLocation, String operation);
}
