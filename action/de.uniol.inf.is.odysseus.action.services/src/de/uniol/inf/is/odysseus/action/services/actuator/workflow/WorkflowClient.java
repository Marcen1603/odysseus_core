package de.uniol.inf.is.odysseus.action.services.actuator.workflow;

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

import de.uniol.inf.is.odysseus.action.services.actuator.ActionMethod;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;


/**
 * Workflowclient based on dynamic jaxb clients created by apache cxf framework.
 * Due to the static structure of webservices there are some restrictions: 
 * - Only one service supported!
 * - Only one binding supported!
 * - Correlations element must include the unique name 'correlation' case-insensitive
 * @author Simon Flandergan
 *
 */
public class WorkflowClient implements IActuator {
	private Client client;
	
	//TODO debug, über konstruktor oder invoke holen!
	private String correlationID = "http://sflan1:8001/soa-infra/services/default/BPELScenario/maintenance_client_ep?WSDL5";
	
	private Map<String, Message> methodMessageMapping;
	private ArrayList<ActionMethod> methods;
	
	private static String correlationName = "correlation";
	
	public WorkflowClient(Client client) throws ActuatorException{
		this.client = client;
		
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
	        	
	        	Message message = new Message(methodName);
	
	        	//create parts for each messagePart
	        	List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		        List<String> parameterNames = new ArrayList<String>();
		        
	        	for (MessagePartInfo partInfo : inputMessageInfo.getMessageInfo().getMessageParts()){
		            Class<?> partClass = partInfo.getTypeClass();
		            
		            //check if this part is a correlation
		            boolean correlation = false;
		            if (partClass.getName().toLowerCase().contains(correlationName)){
		            	correlation = true;
		            }
		            MessagePart messagePart = new MessagePart(methodName, partClass, correlation);
		            
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
		            message.addMessagePart(messagePart);    
	        	}
	        	//create method for schema
	        	ActionMethod method = new ActionMethod(methodName.getLocalPart(),
	            			parameterTypes.toArray(new Class<?>[parameterTypes.size()]), 
	            			parameterNames.toArray(new String[parameterNames.size()]), true);
	        	this.methods.add(method);
	        	
	        	//add message to mapping
	        	this.methodMessageMapping.put(methodName.getLocalPart(), message);
	            
   
	        }
		}catch (Exception e){
			throw new ActuatorException(e.getMessage());
		}
	}
	
	public WorkflowClient(Client client, String correlationID) throws ActuatorException{
		this(client);
		this.correlationID = correlationID;
	}

	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) throws ActuatorException {
		try {
			Message message = this.methodMessageMapping.get(method);

			//fill parts with values by splitting param array
			List<MessagePart> parts = message.getParts();
			int startIndex = 0;
			
			Object[] inputs = new Object[parts.size()];
			int inputIndex = 0;
			
			for (MessagePart part : parts){
				//insert correlationID for correlation parts or attributes for other parts
				if (part.isCorrelation()){
					part.setValsForProperties(new String[]{this.correlationID});
				}else {
					int endIndex = startIndex+part.getNumberOfProperties();
					part.setValsForProperties(Arrays.copyOfRange(params, startIndex, endIndex));
					startIndex = endIndex;
				}
				
				inputs[inputIndex] = part.getInputObject();
				inputIndex++;
			}

			Object[] result = client.invoke(message.getMessageName(), inputs);

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

}
