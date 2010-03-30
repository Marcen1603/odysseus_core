
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.5
 * Tue Mar 30 15:25:18 CEST 2010
 * Generated source version: 2.2.5
 * 
 */

@WebFault(name = "fault", targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/")
public class CreateSourceFault extends Exception {
    public static final long serialVersionUID = 20100330152518L;
    
    private de.uniol.inf.is.odysseus.webservice.Fault fault;

    public CreateSourceFault() {
        super();
    }
    
    public CreateSourceFault(String message) {
        super(message);
    }
    
    public CreateSourceFault(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateSourceFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault) {
        super(message);
        this.fault = fault;
    }

    public CreateSourceFault(String message, de.uniol.inf.is.odysseus.webservice.Fault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public de.uniol.inf.is.odysseus.webservice.Fault getFaultInfo() {
        return this.fault;
    }
}
