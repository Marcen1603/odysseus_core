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
	private static long actuatorCount = 0;
	private static IAdvancedExecutor executor;
	private static IActuatorFactory actuatorFactory;
	
	/**
	 * Used by osgi for binding the executor
	 * @param executor
	 */
    public void bindExecutor(IAdvancedExecutor executor) {
		OdysseusWSPortImpl.executor = executor;
	}
    
    /**
     * Used by osgi for binding the actuator service 
     * @param factory
     */
    public void bindActuatorFactory(IActuatorFactory factory){
    	actuatorFactory = factory;
    }
    
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createActuator(de.uniol.inf.is.odysseus.webservice.ActuatorInformation  actuator )*
     */
    public java.lang.String createActuator(ActuatorInformation actuator) throws ActuatorServiceFault , ActuatorFault , ActuatorManagerFault  { 
    	try {
    		String actuatorName = actuator.getActuatorName();
    		if (actuatorName == null || actuatorName.trim().length() == 0){
    			//generate actuatorName
    			actuatorName = actuator.getManagerName();
    			actuatorCount++;
    			actuatorName = actuatorName.concat(""+actuatorCount);
    		}
    		actuatorFactory.createActuator(
    				actuatorName, 
    				actuator.getDescription(),
    				actuator.getManagerName());
    		return actuatorName;
    	}catch (ActuatorException e){
    		throw new ActuatorFault(e.getMessage());
    	}catch (NullPointerException e){
    		throw new ActuatorManagerFault();
    	}catch (Exception e){
    		throw new ActuatorServiceFault(e.getMessage());
    	}
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createStatement(de.uniol.inf.is.odysseus.webservice.QueryType  query )*
     */
    public java.lang.String createStatement(QueryType query) throws StatementQueryFault , StatementServiceFault    { 
        try {
			Integer queryID = executor.addQuery(query.getQuery(), query.getLanguage(), 
					new ParameterParserID(query.getLanguage())).iterator().next();
			return queryID.toString();
		} catch (PlanManagementException e) {
			throw new StatementQueryFault(e.getMessage());
		} catch (NullPointerException e){
			throw new StatementServiceFault("Querymanagement not avaiable");
		}catch (Exception e){
    		throw new StatementServiceFault(e.getMessage());
    	}
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createSource(java.lang.String  sourceDescription )*
     */
    public java.lang.String createSource(java.lang.String sourceDescription) throws SourceQueryFault , SourceServiceFault    { 
       //TODO implementieren ....
    	throw new SourceServiceFault();
    }


    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#getSchema(java.lang.String  id )*
     */
    public java.lang.String[] getSchema(java.lang.String id) throws SchemaServiceFault , SchemaIDFault    { 
        try {
        	IQuery query = executor.getSealedPlan().getQuery(Integer.valueOf(id));
        	SDFAttributeList outputSchema = query.getSealedLogicalPlan().getOutputSchema();
        	ArrayList<String> schema = new ArrayList<String>();
        	for (int i=0; i<outputSchema.getAttributeCount(); i++){
        		SDFAttribute attribute = outputSchema.getAttribute(i);
        		schema.add(attribute.getAttributeName());
        		schema.add(attribute.getDatatype().getURI());
        	}
        	return schema.toArray(new String[schema.size()]);
        }catch (Exception e){
    		throw new SchemaServiceFault(e.getMessage());
    	}
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#removeActuator(de.uniol.inf.is.odysseus.webservice.ActuatorReducedInformation  actuator )*
     */
    public java.lang.String removeActuator(ActuatorReducedInformation actuator) throws RemoveActuatorFault    { 
        try {
        	actuatorFactory.removeActuator(actuator.getActuatorName(), actuator.getManagerName());
        	return "";
        }catch (Exception e){
        	throw new RemoveActuatorFault();
        }
    }

}
