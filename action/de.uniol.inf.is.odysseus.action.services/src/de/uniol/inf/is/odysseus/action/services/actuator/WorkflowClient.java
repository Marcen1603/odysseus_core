package de.uniol.inf.is.odysseus.action.services.actuator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.Binding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.ServiceInfo;



public class WorkflowClient implements IActuator {
	private Client client;
	private String correlationID;
	
	public WorkflowClient(Client client){
		this.client = client;
		
		//build schema
		ClientImpl clientImpl = (ClientImpl) client;
        Endpoint endpoint = clientImpl.getEndpoint();
        Service service = endpoint.getService();
        ServiceInfo serviceInfo = service.getServiceInfos().get(0);
        Map<QName, MessageInfo> messages = serviceInfo.getMessages();
        for (Entry<QName, MessageInfo> message : messages.entrySet()){
        	System.out.print(message.getKey().getNamespaceURI()+" || "+message.getValue().getMessagePart(0));
        }
	}
	
	public WorkflowClient(Client client, String correlationID){
		this(client);
		this.correlationID = correlationID;
	}

	@Override
	public void executeMethod(String method, Class<?>[] types, Object[] params) {
		
		
	}

	@Override
	public List<ActionMethod> getFullSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionMethod> getReducedSchema() {
		// TODO Auto-generated method stub
		return null;
	}

}
