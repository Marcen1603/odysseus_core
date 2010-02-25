
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.5
 * Thu Feb 25 14:13:12 CET 2010
 * Generated source version: 2.2.5
 * 
 */

@WebFault(name = "fault", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
public class ActuatorFault extends Exception {
    public static final long serialVersionUID = 20100225141312L;
    
    private de.uniol.inf.is.odysseus.webservice.Fault fault;

    public ActuatorFault() {
        super();
    }
    
    public ActuatorFault(String message) {
        super(message);
    }
    
    public ActuatorFault(String message, Throwable cause) {
        super(message, cause);
    }

    public ActuatorFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault) {
        super(message);
        this.fault = fault;
    }

    public ActuatorFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public de.uniol.inf.is.odysseus.webservice.Fault getFaultInfo() {
        return this.fault;
    }
}
