package de.uniol.inf.is.odysseus.action.actuatorManagement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import de.uniol.inf.is.odysseus.action.exception.ActuatorException;
import de.uniol.inf.is.odysseus.action.output.IActuator;

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
	public void createActuator(String name, String description) throws ActuatorException{
		WorkflowClient client = this.clients.get(description);
		if (client == null){
			try {
				URL url = new URL(description);
				client = new WorkflowClient(factory.createClient(url));
				this.clients.put(name, client);			
			} catch (MalformedURLException e) {
				throw new ActuatorException(e.getMessage());
			} 
		}
	}

	@Override
	public List<ActuatorAdapterMethod> getSchema(String name) {
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


}
