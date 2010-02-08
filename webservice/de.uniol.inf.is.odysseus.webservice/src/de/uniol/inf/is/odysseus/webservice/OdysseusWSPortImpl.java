package de.uniol.inf.is.odysseus.webservice;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Implementation of the generated webservice
 * TODO return um query ids, erweitern
 * @author Simon Flandergan
 */

@javax.jws.WebService(
                      serviceName = "OdysseusWS",
                      portName = "OdysseusWSSOAP",
                      targetNamespace = "http://de.uni.ol.inf.is.odysseus/OdysseusWS/",
                      wsdlLocation = "resources/OdysseusWebService.wsdl",
                      endpointInterface = "de.uniol.inf.is.odysseus.webservice.OdysseusWSPort")
                      
public class OdysseusWSPortImpl implements OdysseusWSPort {
	private IAdvancedExecutor executor;
	private IActuatorFactory actuatorFactory;
	
	/**
	 * Status Codes for webservice calls
	 * @author Simon Flandergan
	 *
	 */
	public enum StatusCode {
		OK("200"), ServiceUnavaible("400"), ParseException("401"), UnknownLanguage("402"),
		UnkownError("499");
		
		private String code;

		StatusCode(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	};
	
	/**
	 * Used by osgi for binding the executor
	 * @param executor
	 */
    public void bindExecutor(IAdvancedExecutor executor) {
		this.executor = executor;
	}
    
    /**
     * Used by osgi for binding the actuator service 
     * @param factory
     */
    public void bindActuatorFactory(IActuatorFactory factory){
    	this.actuatorFactory = factory;
    }
    
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createActuator(de.uniol.inf.is.odysseus.webservice.ActuatorInformation  actuator )*
     */
    public java.lang.String createActuator(ActuatorInformation actuator) { 
    	try {
    		this.actuatorFactory.createActuator(
    				actuator.getActuatorName(), 
    				actuator.getDescription(),
    				actuator.getManagerName());
    	}catch (ActuatorException e){
    		return StatusCode.ParseException.getCode();
    	}catch (NullPointerException e){
    		return StatusCode.ServiceUnavaible.getCode();
    	}catch (Exception e){
    		return StatusCode.UnkownError.getCode();
    	}
		return StatusCode.OK.getCode();
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createStatement(de.uniol.inf.is.odysseus.webservice.QueryType  query )*
     */
    public java.lang.String createStatement(QueryType query) { 
        try {
			this.executor.addQuery(query.getQuery(), query.getLanguage(), 
					new ParameterParserID(query.getLanguage()));
		} catch (PlanManagementException e) {
			return StatusCode.ParseException.getCode();
		} catch (NullPointerException e){
			return StatusCode.ServiceUnavaible.getCode();
		}catch (Exception e){
    		return StatusCode.UnkownError.getCode();
    	}
        return StatusCode.OK.getCode();
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createSource(java.lang.String  sourceDescription )*
     */
    public java.lang.String createSource(java.lang.String sourceDescription) { 
       //TODO implementieren ....
    	return StatusCode.UnkownError.getCode();
    }


    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#getSchema(java.lang.String  id )*
     */
    public java.lang.String[] getSchema(java.lang.String id) { 
        try {
        	IQuery query = this.executor.getSealedPlan().getQuery(Integer.valueOf(id));
        	SDFAttributeList outputSchema = query.getSealedLogicalPlan().getOutputSchema();
        	ArrayList<String> schema = new ArrayList<String>();
        	schema.add(StatusCode.OK.getCode());
        	for (int i=0; i<outputSchema.getAttributeCount(); i++){
        		SDFAttribute attribute = outputSchema.getAttribute(i);
        		schema.add(attribute.getAttributeName());
        		schema.add(attribute.getDatatype().getQualName());
        	}
        	return schema.toArray(new String[schema.size()]);
        }catch (Exception e){
    		return new String[]{StatusCode.UnkownError.getCode()};
    	}
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#removeActuator(de.uniol.inf.is.odysseus.webservice.ActuatorReducedInformation  actuator )*
     */
    public java.lang.String removeActuator(ActuatorReducedInformation actuator) { 
        try {
        	this.actuatorFactory.removeActuator(actuator.getActuatorName(), actuator.getManagerName());
        }catch (ActuatorException e){
        	return StatusCode.ParseException.getCode();
        }catch (NullPointerException e){
        	return StatusCode.ServiceUnavaible.getCode();
        }catch (Exception e){
    		return StatusCode.UnkownError.getCode();
    	}
        return StatusCode.OK.getCode();
    }

}
