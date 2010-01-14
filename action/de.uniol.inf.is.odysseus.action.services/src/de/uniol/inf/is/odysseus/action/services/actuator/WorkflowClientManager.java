package de.uniol.inf.is.odysseus.action.services.actuator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;

/**
 * ActuatorManager for WorkflowClients
 * Descriptions must be a URL poiting to a WSDL
 * @author Simon Flandergan
 *
 */
public class WorkflowClientManager implements IActuatorManager {
	private JaxWsDynamicClientFactory factory;
	private HashMap<String, WorkflowClient> clients;

	
	public WorkflowClientManager(){
		this.factory = JaxWsDynamicClientFactory.newInstance();
		this.clients = new HashMap<String, WorkflowClient>();
	}


	@Override
	public IActuator createActuator(String name, String description) throws ActuatorException{
		WorkflowClient client = this.clients.get(description);
		if (client == null){
			try {
				URL url = new URL(description);
				client = new WorkflowClient(factory.createClient(url));
				this.clients.put(name, client);		
				return client;
			} catch (MalformedURLException e) {
				throw new ActuatorException(e.getMessage());
			} 
		}
		return client;
	}

	@Override
	public List<ActionMethod> getSchema(String name) {
		WorkflowClient client = this.clients.get(name);
		if (client != null){
			return client.getSchema();
		}
		return null;
	}


	@Override
	public String getName() {
		return "WorkflowClientManager";
	}


	@Override
	public IActuator getActuator(String name) throws ActuatorException {
		IActuator client = this.clients.get(name);
		if (client != null){
			return client;
		}
		throw new ActuatorException("Client <"+name+"> does not exist");
	}


	@Override
	public void removeActuator(String name) throws ActuatorException{
		if (this.clients.remove(name) == null){
			throw new ActuatorException(this.getName()+": Actuator <"+name+"> does not exist");
		}
	}


	@Override
	public List<String> getRegisteredActuatorNames() {
		return new ArrayList<String>(this.clients.keySet());
	}


}
