package de.uniol.inf.is.odysseus.webservice;

import java.util.List;

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.ActionParameter;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterParserID;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Implementation of the generated webservice
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
	
	/* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createStatement(de.uniol.inf.is.odysseus.webservice.QueryType  query )*
     */
	public int addStatement(QueryType query) throws StatementQueryFault    { 
        try {
			Integer queryID = executor.addQuery(query.getQuery(), query.getLanguage(), 
					new ParameterParserID(query.getLanguage())).iterator().next();
			return queryID;
		} catch (Exception e) {
			throw new StatementQueryFault(e.getMessage());
		}
    }
    
    /**
     * Used by osgi for binding the actuator service 
     * @param factory
     */
    public void bindActuatorFactory(IActuatorFactory factory){
    	actuatorFactory = factory;
    }
    
    
    /**
	 * Used by osgi for binding the executor
	 * @param executor
	 */
    public void bindExecutor(IAdvancedExecutor executor) {
		OdysseusWSPortImpl.executor = executor;
	}
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createActuator(de.uniol.inf.is.odysseus.webservice.ActuatorInformation  actuator )*
     */
    public java.lang.String createActuator(ActuatorInformation actuator) throws ActuatorFault    {  
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
    	}catch (Exception e){
    		throw new ActuatorFault(e.getMessage());
    	}
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#createSource(java.lang.String  sourceDescription )*
     */
    public java.lang.String createSource(java.lang.String sourceDescription) throws CreateSourceFault    { 
       //TODO implementieren ....
    	throw new CreateSourceFault();
    }


    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#getSchema(java.lang.String  id )*
     */
    public de.uniol.inf.is.odysseus.webservice.SchemaArray getSchema(java.lang.String queryID) throws QueryIDFault    { 
        try {
        	IQuery query = executor.getSealedPlan().getQuery(Integer.valueOf(queryID));
        	SDFAttributeList outputSchema = query.getSealedLogicalPlan().getOutputSchema();
        	ObjectFactory factory = new ObjectFactory();
        	
        	SchemaArray schemaArray = factory.createSchemaArray();
        	//initialize schemaElement list
        	List<SchemaElementType> schemaElements = schemaArray.getSchemaElement();
        	
        	for (int i=0; i<outputSchema.getAttributeCount(); i++){
        		SDFAttribute attribute = outputSchema.getAttribute(i);
        		
        		SchemaElementType schemaElement = factory.createSchemaElementType();
        		schemaElement.setType(attribute.getDatatype().getQualName());
        		schemaElement.setIdentifier(attribute.getAttributeName());
        		schemaElements.add(schemaElement);
        	}
        	return schemaArray;
        	
        }catch (Exception e){
    		throw new QueryIDFault(e.getMessage());
    	}
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#removeActuator(de.uniol.inf.is.odysseus.webservice.ActuatorReducedInformation  actuator )*
     */
    public java.lang.String removeActuator(ActuatorReducedInformation actuator) throws NonExistingActuatorFault    { 
        try {
        	actuatorFactory.removeActuator(actuator.getActuatorName(), actuator.getManagerName());
        	return "";
        }catch (Exception e){
        	throw new NonExistingActuatorFault();
        }
    }
    
    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#removeStatement(java.math.BigInteger  queryID )*
     */
    public java.lang.String removeStatement(java.math.BigInteger queryID) throws NonExistingStatementFault    {  
        try {
        	executor.removeQuery(queryID.intValue());
        	return "";
        }catch (Exception e){
        	throw new NonExistingStatementFault(e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.webservice.OdysseusWSPort#getActuatorSchema(de.uniol.inf.is.odysseus.webservice.ActuatorReducedInformation  actuator )*
     */
    public de.uniol.inf.is.odysseus.webservice.ActuatorSchemaArray getActuatorSchema(ActuatorReducedInformation actuator) throws ActuatorDoesNotExistFault    {
		try {
			IActuator refActuator = actuatorFactory.getActuator(actuator.getActuatorName(),actuator.getManagerName());
			
			ObjectFactory factory = new ObjectFactory();
        	
        	ActuatorSchemaArray schemaArray = factory.createActuatorSchemaArray();
        	//initialize schemaElement list
        	List<Method> methods = schemaArray.getMethod();
        	
        	List<ActionMethod> schema = refActuator.getReducedSchema();
        	if (schema.isEmpty()){
        		schema = refActuator.getFullSchema();
        	}
        	
        	for (ActionMethod method : schema){
        		Method actuatorMethod = factory.createMethod();
        		actuatorMethod.setMethodName(method.getName());
        		
        		List<ParameterType> params = actuatorMethod.getParameter();
        		//add params
        		for (ActionParameter param : method.getParameters()){
        			ParameterType paramType = factory.createParameterType();
        			paramType.setParamName(param.getName());
        			paramType.setParamType(param.getType().toString());
        			
        			params.add(paramType);
        		}
        		
        		methods.add(actuatorMethod);
        		
        	}
        	return schemaArray;
        	
		} catch (ActuatorException e) {
			throw new ActuatorDoesNotExistFault(e.getMessage());
		}
       
    }

}
