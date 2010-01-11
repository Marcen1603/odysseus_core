
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package de.uniol.inf.is.odysseus.webservice;

import java.util.logging.Logger;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;

/**
 * This class was generated by Apache CXF 2.2.5
 * Wed Dec 09 10:34:56 CET 2009
 * Generated source version: 2.2.5
 * 
 */

@javax.jws.WebService(
                      serviceName = "OdysseusWS",
                      portName = "OdysseusWSSOAP",
                      targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/",
                      //wsdlLocation = "file:OdysseusWebService.wsdl",
                      endpointInterface = "de.uniol.inf.is.odysseus.webservice.OdysseusWSPort")
                      
public class OdysseusWSPortImpl implements OdysseusWSPort {

    private static final Logger LOG = Logger.getLogger(OdysseusWSPortImpl.class.getName());
	private IAdvancedExecutor executor;

    
    public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createStatement(de.uniol.inf.is.odysseus.webservice.QueryType  query )*
     */
    public java.lang.String createStatement(QueryType query) { 
        try {
			this.executor.addQuery(query.getQuery(), query.getLanguage(), null);
		} catch (PlanManagementException e) {
			return e.getMessage();
		}
        return "SUCCESS";
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createSource(java.lang.String  sourceDescription )*
     */
    public java.lang.String createSource(java.lang.String sourceDescription) { 
        LOG.info("Executing operation createSource");
        System.out.println(sourceDescription);
        try {
            java.lang.String _return = "";
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#getSchema(java.lang.String  id )*
     */
    public java.lang.String[] getSchema(java.lang.String id) { 
        LOG.info("Executing operation getSchema");
        System.out.println(id);
        try {
            java.lang.String[] _return = new java.lang.String[0];
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
