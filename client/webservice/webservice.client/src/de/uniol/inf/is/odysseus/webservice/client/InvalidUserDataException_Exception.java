
package de.uniol.inf.is.odysseus.webservice.client;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InvalidUserDataException", targetNamespace = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/")
public class InvalidUserDataException_Exception
    extends Exception
{


	private static final long serialVersionUID = 2713901793192142489L;
	/**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidUserDataException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public InvalidUserDataException_Exception(String message, InvalidUserDataException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public InvalidUserDataException_Exception(String message, InvalidUserDataException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: de.uniol.inf.is.odysseus.webservice.client.InvalidUserDataException
     */
    public InvalidUserDataException getFaultInfo() {
        return faultInfo;
    }

}
