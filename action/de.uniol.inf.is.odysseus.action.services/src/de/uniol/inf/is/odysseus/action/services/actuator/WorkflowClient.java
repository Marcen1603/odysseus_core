package de.uniol.inf.is.odysseus.action.services.actuator;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;


/**
 * Workflowclient based on dynamic jaxb clients created by apache cxf framework.
 * Restrictions: 
 * - Only one service supported!
 * - Only one binding supported!
 * - Only one part in message supported (standard for bpel)
 * @author Simon Flandergan
 *
 */
public class WorkflowClient implements IActuator {
	private Client client;
	private String correlationID;
	
	private Map<String, MessagePart> methodPartMapping;
	private ArrayList<ActionMethod> methods;
	
	public WorkflowClient(Client client) throws ActuatorException{
		this.client = client;
		
		this.methodPartMapping = new HashMap<String, MessagePart>();
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
	        	
	            // only first part supported!
	            MessagePartInfo partInfo = inputMessageInfo.getMessageParts().get(0);
	            Class<?> partClass = partInfo.getTypeClass();
	            
	            MessagePart messagePart = new MessagePart(methodName, partClass);
	            
	            //addPropertyDescriptors for each field and remember field type as parameter
	            Field[] fields = partClass.getDeclaredFields();
	            int counter = 0;
	            
	            Class<?>[] parameterTypes = new Class<?>[fields.length];
	            for (Field field : fields){
	            	messagePart.addPropertyDescriptor(field.getName());
	            	
	            	parameterTypes[counter] = field.getType();
	            	counter++;
	            }
	            
	            //add messagepart
	            this.methodPartMapping.put(methodName.getLocalPart(), messagePart);
	          
	            //create method for schema
	            ActionMethod method = new ActionMethod(methodName.getLocalPart(),
            			parameterTypes, true);
	            this.methods.add(method);
   
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
	public void executeMethod(String method, Class<?>[] types, Object[] params) {
		
		
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
