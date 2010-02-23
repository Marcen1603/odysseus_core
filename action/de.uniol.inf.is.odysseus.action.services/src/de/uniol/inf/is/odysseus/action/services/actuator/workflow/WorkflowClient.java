package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;


/**
 * Workflowclient based on dynamic jaxb clients created by apache cxf framework.
 * Due to the static structure of webservices there are some restrictions: 
 * - Only one service supported!
 * - Only one binding supported!
 * - Correlations element must include the unique name 'correlation' case-insensitive
 * - All elements from possible results are ignored, besides correlations
 * @author Simon Flandergan
 *
 */
public class WorkflowClient implements IActuator {
	private Client client;
	
	private String correlationID;
	
	private Map<String, Message> methodMessageMapping;
	private ArrayList<ActionMethod> methods;

	private Logger logger;
	
	private static String correlationName = "correlation";
	
	/**
	 * Creates a new WorkflowClient without correlation information
	 * @param client
	 * @throws ActuatorException
	 */
	public WorkflowClient(Client client) throws ActuatorException{
		this.client = client;
		
		this.logger = LoggerFactory.getLogger(WorkflowClient.class);
		
		this.methodMessageMapping = new HashMap<String, Message>();
		this.methods = new ArrayList<ActionMethod>();
		
		try {
			//get supported operations
			ClientImpl clientImpl = (ClientImpl) client;
	        Endpoint endpoint = clientImpl.getEndpoint();
	        
	        ServiceInfo serviceInfo = endpoint.getService().getServiceInfos().get(0);
	        BindingInfo binding = serviceInfo.getBindings().iterator().next();
	        
	        for (BindingOperationInfo operation : binding.getOperations()){       	
	        	QName methodName = operation.getName();    	
	        	BindingMessageInfo inputMessageInfo = operation.getInput();
	        	BindingMessageInfo outputMessageInfo = operation.getOutput();
	        	
	        	Message message = new Message(methodName);
	
	        	if (inputMessageInfo != null){
	        		this.createInputMessageParts(inputMessageInfo, message);
	        	}
	        	if (outputMessageInfo != null){
	        		this.createOutputMessageParts(outputMessageInfo, message);
	        	}
	        	
	        	//add message to mapping
	        	this.methodMessageMapping.put(methodName.getLocalPart(), message);
	            
   
	        }
		}catch (Exception e){
			throw new ActuatorException(e.getMessage());
		}
	}
	
	/**
	 * Creates a new WorkflowClient with correlation information
	 * @param client
	 * @param correlationID
	 * @throws ActuatorException
	 */
	public WorkflowClient(Client client, String correlationID) throws ActuatorException{
		this(client);
		this.correlationID = correlationID;
	}

	private void createInputMessageParts(BindingMessageInfo inputMessageInfo, Message message) throws InstantiationException, IllegalAccessException, IntrospectionException {
    	List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
        List<String> parameterNames = new ArrayList<String>();
        
    	for (MessagePartInfo partInfo : inputMessageInfo.getMessageInfo().getMessageParts()){
            Class<?> partClass = partInfo.getTypeClass();
            
            //check if this part is a correlation
            boolean correlation = false;
            if (partClass.getName().toLowerCase().contains(correlationName)){
            	correlation = true;
            }
            MessagePart messagePart = new MessagePart(partClass.newInstance(), correlation);
            
            //addPropertyDescriptors for each field and remember field type as parameter
            for (Field field : partClass.getDeclaredFields()){
            	String fieldName = field.getName();
            	messagePart.addPropertyDescriptor(fieldName);
            	
            	//only add if messagePart is not the correlation!
            	if (!correlation){
            		parameterTypes.add(field.getType());
            		parameterNames.add(fieldName);
            	}
            }
            
            //add messagepart to message
            message.addInputMessagePart(messagePart);    
    	}
    	
    	//create method for schema
    	ActionMethod method = new ActionMethod(message.getMessageName().getLocalPart(),
        			parameterTypes.toArray(new Class<?>[parameterTypes.size()]), 
        			parameterNames.toArray(new String[parameterNames.size()]), true);
    	this.methods.add(method);
	}
	
	private void createOutputMessageParts(BindingMessageInfo outputMessageInfo,
			Message message) throws InstantiationException, IllegalAccessException, IntrospectionException {
		for (MessagePartInfo partInfo : outputMessageInfo.getMessageInfo().getMessageParts()){
            Class<?> partClass = partInfo.getTypeClass();
            
            //check if this part is a correlation
            boolean correlation = false;
            if (partClass.getName().toLowerCase().contains(correlationName)){
            	correlation = true;
            }
            MessagePart messagePart = new MessagePart(partClass.newInstance(), correlation);
            
            //addPropertyDescriptors for each field and remember field type as parameter
            for (Field field : partClass.getDeclaredFields()){
            	String fieldName = field.getName();
            	messagePart.addPropertyDescriptor(fieldName);
            }
            
            //add messagepart to message
            message.addOutputMessagePart(messagePart);    
    	}
	}

	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Message message = this.methodMessageMapping.get(method);

			//fill parts with values by splitting param array
			List<MessagePart> parts = message.getInputParts();
			int startIndex = 0;
			
			Object[] inputs = new Object[parts.size()];
			int inputIndex = 0;
			
			for (MessagePart part : parts){
				//insert correlationID for correlation parts or attributes for other parts
				if (part.isCorrelation()){
					if (this.correlationID != null){
						part.setValsForProperties(new String[]{this.correlationID});
					}else {
						throw new ActuatorException("Cannot send message: CorrelationID is missing");
					}
				}else {
					int endIndex = startIndex+part.getNumberOfProperties();
					part.setValsForProperties(Arrays.copyOfRange(params, startIndex, endIndex));
					startIndex = endIndex;
				}
				
				inputs[inputIndex] = part.getInputObject();
				inputIndex++;
			}
			//invoke message
			Object[] result = client.invoke(message.getMessageName(), inputs);
			
			this.logger.debug("Invoked: "+message.getMessageName()+ " \nwith Correlation: "+this.correlationID +"\n" +
					"target: "+ this.client.getEndpoint().getEndpointInfo().getAddress());
			
			if (result != null){
				this.interpretResult(result, message);	
			}

		}catch (NullPointerException e){
			throw new ActuatorException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ActuatorException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ActuatorException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new ActuatorException(e.getMessage());
		} catch (Exception e) {
			throw new ActuatorException(e.getMessage());
		}
		
	}

	@Override
	public List<ActionMethod> getFullSchema() {
		return this.methods;
	}

	@Override
	public List<ActionMethod> getReducedSchema() {
		ArrayList<ActionMethod> reducedSchema = new ArrayList<ActionMethod>();
		for (ActionMethod m : this.methods){
			if (m.isShowInSchema()){
				reducedSchema.add(m);
			}
			
		}
		return reducedSchema;
	}

	private void interpretResult(Object[] result, Message message) throws ActuatorException {
		for (Object res : result){
			//find corresponding message part
			Class<?> resultClass = res.getClass();
			for (MessagePart part : message.getOutputParts()){
				if (part.getPartClass().equals(resultClass)){
					//only interpret correlation results
					if (part.isCorrelation()){
						try {
							Object val = part.getValsForProperties(res).values().iterator().next();
							if (val != null){
								this.correlationID = (String)val;
							}
						} catch (IllegalArgumentException e) {
							throw new ActuatorException("Interpreting result failed: " +e.getMessage());
						} catch (IllegalAccessException e) {
							throw new ActuatorException("Interpreting result failed: " +e.getMessage());
						} catch (InvocationTargetException e) {
							throw new ActuatorException("Interpreting result failed: " +e.getMessage());
						} catch (ClassCastException e){
							throw new ActuatorException("Interpreting result failed: " +e.getMessage());
						}
					}
				}
			}
		}
	}

}
