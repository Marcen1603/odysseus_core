package de.uniol.inf.is.odysseus.wsenrich.util;

import com.eviware.soapui.model.iface.Operation;

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
	 * @return the amount of all operations found in the wsdl-file
	 */
	public Operation[] getOperationsOfWsdl();
	
	/**
	 * @return the amount of all messages found in the wsdl-file
	 */
	public String[] getEndpointAdress();
	

}
