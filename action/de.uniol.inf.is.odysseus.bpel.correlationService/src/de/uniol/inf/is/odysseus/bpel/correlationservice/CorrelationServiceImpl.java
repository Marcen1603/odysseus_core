
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package de.uniol.inf.is.odysseus.bpel.correlationservice;

import java.util.HashMap;

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
	private HashMap<String, Integer> correlations;


	public CorrelationServiceImpl(){
		this.correlations = new HashMap<String, Integer>();
	}
  

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.bpel.correlationservice.CorrelationService#getCorrelation(java.lang.String  uri )*
     */
    public java.lang.String getCorrelation(java.lang.String uri) { 
       Integer idNo = this.correlations.get(uri);
       if (idNo == null){
    	   idNo = 0;
       }else {
    	   idNo++;
       }    
       this.correlations.put(uri, idNo);
       return uri+idNo;
    }

}
