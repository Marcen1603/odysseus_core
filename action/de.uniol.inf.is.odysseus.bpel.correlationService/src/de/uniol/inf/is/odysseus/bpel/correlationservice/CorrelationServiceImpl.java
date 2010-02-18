
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package de.uniol.inf.is.odysseus.bpel.correlationservice;

import java.util.Random;

/**
 * Implementation of the webservice
 * for providing bpel correlation ids
 * @author Simon Flandergan
 */

@javax.jws.WebService(
                      serviceName = "correlationService",
                      portName = "correlationServiceSOAP",
                      targetNamespace = "http://de.uni.ol.inf.is.odysseus/correlationService/",
                      wsdlLocation = "resources/correlationservice.wsdl",
                      endpointInterface = "de.uniol.inf.is.odysseus.bpel.correlationservice.CorrelationService")
                      
public class CorrelationServiceImpl implements CorrelationService {
	private Random randomGen;

	public CorrelationServiceImpl() {
		this.randomGen = new Random();
	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.bpel.correlationservice.CorrelationService#getCorrelation(java.lang.String  uri )*
     */
    public java.lang.String getCorrelation(java.lang.String uri) { 
       int sIndex = this.randomGen.nextInt(uri.length());
       int eIndex = sIndex + this.randomGen.nextInt(uri.length()-sIndex);
       return ""+uri.substring(sIndex, eIndex).hashCode()+System.currentTimeMillis();
    }

}
